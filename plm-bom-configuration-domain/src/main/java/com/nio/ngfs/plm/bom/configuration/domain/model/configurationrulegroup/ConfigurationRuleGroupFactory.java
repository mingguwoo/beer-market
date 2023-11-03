package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

import java.util.Optional;

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
                .constrainedFeature(Joiner.on(",").skipNulls().join(Optional.ofNullable(cmd.getConstrainedFeatureList()).orElse(Lists.newArrayList())))
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

}
