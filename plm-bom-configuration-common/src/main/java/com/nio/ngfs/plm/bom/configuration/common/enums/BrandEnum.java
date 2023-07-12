package com.nio.ngfs.plm.bom.configuration.common.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
public enum BrandEnum {

    /**
     * 品牌类型
     */
    NIO,
    ALPS,
    FY;

    public static BrandEnum getByName(String name) {
        return EnumUtils.getEnum(BrandEnum.class, BrandEnum::name, name);
    }

}
