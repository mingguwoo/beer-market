package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum FeatureTypeEnum {

    /**
     * Feature类型
     */
    GROUP("Group", "Feature"),
    FEATURE("Feature", "Option"),
    OPTION("Option", null);

    private final String type;
    private final String childrenType;

    FeatureTypeEnum(String type, String childrenType) {
        this.type = type;
        this.childrenType = childrenType;
    }

    public String getType() {
        return type;
    }

    public String getChildrenType() {
        return childrenType;
    }

    public static FeatureTypeEnum getByType(String type) {
        return EnumUtils.getEnum(FeatureTypeEnum.class, FeatureTypeEnum::getType, type);
    }

}
