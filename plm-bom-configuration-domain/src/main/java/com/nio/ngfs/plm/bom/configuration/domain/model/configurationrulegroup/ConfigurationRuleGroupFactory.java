package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.google.common.base.Joiner;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public class ConfigurationRuleGroupFactory {

    public static ConfigurationRuleGroupAggr create(AddRuleCmd cmd) {
        return ConfigurationRuleGroupAggr.builder()
                .chineseName(cmd.getChineseName())
                .displayName(cmd.getDisplayName())
                .purpose(cmd.getPurpose())
                .definedBy(cmd.getDefinedBy())
                .description(cmd.getDescription())
                .drivingFeature(cmd.getDrivingFeature())
                .constrainedFeature(Joiner.on(",").skipNulls().join(cmd.getConstrainedFeatureList()))
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

}
