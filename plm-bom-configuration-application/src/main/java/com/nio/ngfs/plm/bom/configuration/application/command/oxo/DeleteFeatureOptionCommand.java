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
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        featureOptionDomainService.checkFeatureOptionDelete(featureOptionAggrList);
        // 构建Feature/Option行的子节点列表
        featureOptionApplicationService.buildFeatureOptionWithChildren(featureOptionAggrList);
        // 在最新Release版本OXO中查询存在的Feature/Option
        Set<String> existFeatureOptionCodeSet = featureOptionApplicationService.queryExistFeatureOptionInLastedReleaseSnapshot(featureOptionAggrList);
        // 物理删除
        List<OxoFeatureOptionAggr> physicalDeleteList = featureOptionAggrList.stream().filter(i -> !existFeatureOptionCodeSet.contains(i.getFeatureCode())).toList();
        physicalDeleteList.forEach(OxoFeatureOptionAggr::physicalDelete);
        // 逻辑删除
        List<OxoFeatureOptionAggr> softDeleteList = featureOptionAggrList.stream().filter(i -> existFeatureOptionCodeSet.contains(i.getFeatureCode())).toList();
        softDeleteList.forEach(OxoFeatureOptionAggr::softDelete);
        // 校验并删除打点
        Pair<List<OxoOptionPackageAggr>, List<String>> result = featureOptionApplicationService.checkAndDeleteOptionPackage(softDeleteList);
        // 待更新的Feature/Option行
        List<OxoFeatureOptionAggr> updateFeatureOptionAggrList = Lists.newArrayList(featureOptionAggrList.iterator());
        featureOptionAggrList.forEach(i -> updateFeatureOptionAggrList.addAll(i.getChildren()));
        // 事务保存到数据库
        ((DeleteFeatureOptionCommand) AopContext.currentProxy()).saveFeatureOptionAndOptionPackage(updateFeatureOptionAggrList, result.getLeft());
        return new DeleteFeatureOptionRespDto(result.getRight());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveFeatureOptionAndOptionPackage(List<OxoFeatureOptionAggr> featureOptionAggrList, List<OxoOptionPackageAggr> optionPackageAggrList) {
        featureOptionRepository.batchSave(featureOptionAggrList);
        oxoOptionPackageRepository.batchSave(optionPackageAggrList);
    }

}
