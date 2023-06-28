package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum FeatureTypeEnum {

    /**
     * Feature类型
     */
    GROUP("Group"),
    FEATURE("Feature"),
    OPTION("Option");

    private final String type;

    FeatureTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
