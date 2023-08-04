package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums;

import com.nio.bom.share.utils.EnumUtils;
import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/8/1
 */
public enum OxoOptionPackageTypeEnum {

    /**
     * 打点类型
     */
    DEFALUT ("●", "Default"),
    UNAVAILABLE("-", "Unavailable"),

    AVAILABLE("○", "Available"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String type;

    OxoOptionPackageTypeEnum(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public static OxoOptionPackageTypeEnum getByType(String type) {
        return EnumUtils.getEnum(OxoOptionPackageTypeEnum.class, OxoOptionPackageTypeEnum::getType, type);
    }
}
