package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/8/1
 */
public enum OxoOptionPackageTypeEnum {

    /**
     * 打点类型
     */
    DEFAULT("Default"),
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable"),
    ;

    /**
     * 类型
     */
    private final String type;

    OxoOptionPackageTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static OxoOptionPackageTypeEnum getByType(String type) {
        return EnumUtils.getEnum(OxoOptionPackageTypeEnum.class, OxoOptionPackageTypeEnum::getType, type);
    }

}
