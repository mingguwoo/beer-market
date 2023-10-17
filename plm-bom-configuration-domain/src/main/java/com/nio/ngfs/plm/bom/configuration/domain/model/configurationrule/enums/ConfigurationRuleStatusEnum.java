package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRuleStatusEnum {

    IN_WORK("In Work"),
    RELEASED("Released");

    private final String status;

    ConfigurationRuleStatusEnum(String status) {
        this.status = status;
    }

}
