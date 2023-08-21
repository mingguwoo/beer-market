package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditInfoCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoEditCommand extends AbstractLockCommand<OxoEditInfoCmd, List<String>> {

    private final OxoOptionPackageRepository oxoOptionPackageRepository;

    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;

    private final OxoFeatureOptionApplicationService featureOptionApplicationService;

    @Override
    protected String getLockKey(OxoEditInfoCmd cmd) {
        return RedisKeyConstant.MODEL_OXO_FEATURE_EDIT_PREFIX + cmd.getModelCode();
    }

    @Override
    protected List<String> executeWithLock(OxoEditInfoCmd cmd) {

        String userName = cmd.getUserName();

        String modelCode= cmd.getModelCode();

        // 更新备注 和 ruleCheck
        if (CollectionUtils.isNotEmpty(cmd.getEditOxoRows())) {
            List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs = Lists.newArrayList();
            cmd.getEditOxoRows().forEach(editCmd -> {
                OxoFeatureOptionAggr optionAggr = new OxoFeatureOptionAggr();

                if (StringUtils.isNotBlank(editCmd.getComments())) {
                    optionAggr.setComment(editCmd.getComments());
                }

                if (StringUtils.isNotBlank(editCmd.getRuleCheck())) {
                    optionAggr.setRuleCheck(editCmd.getRuleCheck());
                }
                optionAggr.setId(editCmd.getRowId());
                optionAggr.setUpdateUser(userName);
                oxoFeatureOptionAggrs.add(optionAggr);
            });

            //更新 oxo列表
            if (CollectionUtils.isNotEmpty(oxoFeatureOptionAggrs)) {
                oxoFeatureOptionRepository.updateOxoFeatureOptions(oxoFeatureOptionAggrs);
            }
        }


        if(CollectionUtils.isNotEmpty(cmd.getEditCmds())) {
            //更新 打点信息
            oxoOptionPackageRepository.insertOxoOptionPackages(
                    OxoOptionPackageFactory.createOxoOptionPackageAggrList(cmd.getEditCmds(), userName));

            //更新 软删除
            featureOptionApplicationService.updateSoftDelete(modelCode);
        }





        // 教研数据
        return featureOptionApplicationService.checkRules(cmd.getModelCode());
    }
}
