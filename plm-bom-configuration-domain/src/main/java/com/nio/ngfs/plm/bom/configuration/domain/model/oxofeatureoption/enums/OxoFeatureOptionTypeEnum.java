package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/31
 */
public enum OxoFeatureOptionTypeEnum {

    /**
     * Feature类型
     */
    FEATURE("Feature"),
    OPTION("Option");

    /**
     * 类型
     */
    private final String type;

    OxoFeatureOptionTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static OxoFeatureOptionTypeEnum getByType(String type) {
        return EnumUtils.getEnum(OxoFeatureOptionTypeEnum.class, OxoFeatureOptionTypeEnum::getType, type);
    }

}
