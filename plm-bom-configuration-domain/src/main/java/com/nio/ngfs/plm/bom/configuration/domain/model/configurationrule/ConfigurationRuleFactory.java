package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public class ConfigurationRuleFactory {

    public static ConfigurationRuleAggr create(AddRuleCmd cmd, List<AddRuleCmd.RuleOptionDto> ruleOptionDtoList) {
        return ConfigurationRuleAggr.builder()
                .purpose(cmd.getPurpose())
                .optionList(LambdaUtil.map(ruleOptionDtoList, option -> create(option, cmd.getCreateUser())))
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();

    }

    private static ConfigurationRuleOptionDo create(AddRuleCmd.RuleOptionDto ruleOptionDto, String createUser) {
        return ConfigurationRuleOptionDo.builder()
                .drivingOptionCode(ruleOptionDto.getDrivingOptionCode())
                .drivingFeatureCode(ruleOptionDto.getDrivingFeatureCode())
                .constrainedOptionCode(ruleOptionDto.getConstrainedOptionCode())
                .constrainedFeatureCode(ruleOptionDto.getConstrainedFeatureCode())
                .matrixValue(ruleOptionDto.getMatrixValue())
                .createUser(createUser)
                .updateUser(createUser)
                .build();
    }

}
