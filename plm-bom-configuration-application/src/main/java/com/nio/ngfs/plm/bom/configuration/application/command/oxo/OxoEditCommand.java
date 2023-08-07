package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditInfoCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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

        List<OxoEditCmd> cmdLists = cmd.getEditCmds();

        String userName =cmd.getUserName();

        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs = Lists.newArrayList();

        // 更新备注 和 ruleCheck
        cmdLists.stream().filter(x ->
                Objects.nonNull(x.getHeadId()) &&
                        (StringUtils.isNotBlank(x.getComments()) ||
                                StringUtils.isNotBlank(x.getRuleCheck()))).toList().forEach(editCmd -> {

            OxoFeatureOptionAggr optionAggr = new OxoFeatureOptionAggr();

            if (StringUtils.isNotBlank(editCmd.getComments())) {
                optionAggr.setComment(editCmd.getComments());
            }

            if (StringUtils.isNotBlank(editCmd.getRuleCheck())) {
                optionAggr.setRuleCheck(editCmd.getRuleCheck());
            }
            optionAggr.setId(editCmd.getHeadId());
            optionAggr.setUpdateUser(userName);
            oxoFeatureOptionAggrs.add(optionAggr);
        });


        //更新 oxo列表
        if (CollectionUtils.isNotEmpty(oxoFeatureOptionAggrs)) {
            oxoFeatureOptionRepository.updateOxoFeatureOptions(oxoFeatureOptionAggrs);
        }


        //更新 打点信息
        oxoOptionPackageRepository.insertOxoOptionPackages(
                OxoOptionPackageFactory.createOxoOptionPackageAggrList(cmdLists, userName));


        return featureOptionApplicationService.checkRules(cmd.getModelCode());
    }
}
