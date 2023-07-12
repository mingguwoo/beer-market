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
    GROUP("Group", null, "Feature"),
    FEATURE("Feature", "Group", "Option"),
    OPTION("Option", "Feature", null);

    private final String type;
    private final String parentType;
    private final String childrenType;

    FeatureTypeEnum(String type, String parentType, String childrenType) {
        this.type = type;
        this.parentType = parentType;
        this.childrenType = childrenType;
    }

    public String getType() {
        return type;
    }

    public String getParentType() {
        return parentType;
    }

    public String getChildrenType() {
        return childrenType;
    }

    public static FeatureTypeEnum getByType(String type) {
        return EnumUtils.getEnum(FeatureTypeEnum.class, FeatureTypeEnum::getType, type);
    }

}
