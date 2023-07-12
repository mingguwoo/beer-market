package com.nio.ngfs.plm.bom.configuration.common.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
public enum CatalogEnum {

    /**
     * 分类
     */
    MARKETING("Marketing"),
    ENGINEERING("Engineering");

    private final String catalog;

    CatalogEnum(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public static CatalogEnum getByCatalog(String catalog) {
        return EnumUtils.getEnum(CatalogEnum.class, CatalogEnum::getCatalog, catalog);
    }

}
