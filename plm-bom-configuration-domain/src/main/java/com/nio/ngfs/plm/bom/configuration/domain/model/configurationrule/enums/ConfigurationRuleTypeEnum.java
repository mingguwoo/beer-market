package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRuleTypeEnum {

    INCLUSIVE("Inclusive"),
    EXCLUSIVE("Exclusive"),
    DEFAULT("Default"),
    AVAILABLE("Available");

    private final String ruleType;

    ConfigurationRuleTypeEnum(String ruleType) {
        this.ruleType = ruleType;
    }

}
