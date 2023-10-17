package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRuleChangeTypeEnum {

    ADD("Add"),
    MODIFY("Modify"),
    REMOVE("Remove");

    private final String changeType;

    ConfigurationRuleChangeTypeEnum(String changeType) {
        this.changeType = changeType;
    }

}
