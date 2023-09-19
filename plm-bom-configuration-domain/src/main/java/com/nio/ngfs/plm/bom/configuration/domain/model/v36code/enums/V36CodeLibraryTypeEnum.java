package com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public enum V36CodeLibraryTypeEnum {

    /**
     * V36 Code类型
     */
    DIGIT("Digit"),
    OPTION("Option");

    /**
     * 类型
     */
    private final String type;

    V36CodeLibraryTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
