package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.DeleteFeatureOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.DeleteFeatureOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 删除Feature/Option行
 *
 * @author xiaozhou.tu
 * @date 2023/7/31
 */
@Component
@RequiredArgsConstructor
public class DeleteFeatureOptionCommand extends AbstractLockCommand<DeleteFeatureOptionCmd, DeleteFeatureOptionRespDto> {

    private final OxoFeatureOptionRepository featureOptionRepository;
    private final OxoFeatureOptionDomainService featureOptionDomainService;
    private final OxoFeatureOptionApplicationService featureOptionApplicationService;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;

    @Override
    protected String getLockKey(DeleteFeatureOptionCmd cmd) {
        return RedisKeyConstant.OXO_FEATURE_OPTION_DELETE_LOCK_KEY_PREFIX + cmd.getModelCode();
    }

    @Override
    protected DeleteFeatureOptionRespDto executeWithLock(DeleteFeatureOptionCmd cmd) {
        // 查询删除的Feature/Option行
        List<OxoFeatureOptionAggr> featureOptionAggrList = featureOptionRepository.queryByModelAndFeatureCodeList(cmd.getModelCode(), cmd.getFeatureCodeList());
        // 检查Feature/Option是否可删除
        featureOptionDomainService.checkFeatureOptionDelete(cmd.getFeatureCodeList(), featureOptionAggrList);
        // 构建Feature/Option行的子节点列表
        featureOptionApplicationService.buildFeatureOptionWithChildren(featureOptionAggrList);
        // 在最新Release版本OXO中查询存在的Feature/Option
        Set<String> existFeatureOptionCodeSet = featureOptionApplicationService.queryExistFeatureOptionInLastedReleaseSnapshot(cmd.getModelCode(), featureOptionAggrList);
        // 逻辑删除
        List<OxoFeatureOptionAggr> physicalDeleteList = featureOptionAggrList.stream().filter(i -> !existFeatureOptionCodeSet.contains(i.getFeatureCode())).toList();
        // 软删除
        List<OxoFeatureOptionAggr> softDeleteList = featureOptionAggrList.stream().filter(i -> existFeatureOptionCodeSet.contains(i.getFeatureCode())).toList();
        softDeleteList.forEach(OxoFeatureOptionAggr::softDelete);
        // 软删除的Feature/Option行，校验并删除打点
        Pair<List<OxoOptionPackageAggr>, List<String>> result = featureOptionApplicationService.checkAndDeleteOptionPackage(cmd.getModelCode(), softDeleteList);
        // 事务保存到数据库
        ((DeleteFeatureOptionCommand) AopContext.currentProxy()).saveFeatureOptionAndOptionPackage(physicalDeleteList, softDeleteList, result.getLeft());
        return new DeleteFeatureOptionRespDto(result.getRight());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFeatureOptionAndOptionPackage(List<OxoFeatureOptionAggr> physicalDeleteList, List<OxoFeatureOptionAggr> softDeleteList, List<OxoOptionPackageAggr> optionPackageAggrList) {
        // 逻辑删除
        featureOptionRepository.batchRemove(buildFeatureAndOptionList(physicalDeleteList));
        // 软删除
        featureOptionRepository.batchSave(buildFeatureAndOptionList(softDeleteList));
        oxoOptionPackageRepository.batchSave(optionPackageAggrList);
    }

    private static List<OxoFeatureOptionAggr> buildFeatureAndOptionList(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (CollectionUtils.isEmpty(featureOptionAggrList)) {
            return Collections.emptyList();
        }
        // Feature和Option行列表
        List<OxoFeatureOptionAggr> featureAndOptionList = Lists.newArrayList(featureOptionAggrList.iterator());
        featureOptionAggrList.forEach(i -> {
            if (i.isFeature() && CollectionUtils.isNotEmpty(i.getChildren())) {
                featureAndOptionList.addAll(i.getChildren());
            }
        });
        return featureAndOptionList;
    }

}
