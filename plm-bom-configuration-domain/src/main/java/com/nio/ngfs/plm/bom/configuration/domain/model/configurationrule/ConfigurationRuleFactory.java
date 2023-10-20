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
        ConfigurationRuleAggr ruleAggr = ConfigurationRuleAggr.builder()
                .purpose(cmd.getPurpose())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
        ruleAggr.setOptionList(LambdaUtil.map(ruleOptionDtoList, option -> create(option, cmd.getCreateUser(), ruleAggr)));
        return ruleAggr;
    }

    private static ConfigurationRuleOptionDo create(AddRuleCmd.RuleOptionDto ruleOptionDto, String createUser, ConfigurationRuleAggr ruleAggr) {
        return ConfigurationRuleOptionDo.builder()
                .rule(ruleAggr)
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
