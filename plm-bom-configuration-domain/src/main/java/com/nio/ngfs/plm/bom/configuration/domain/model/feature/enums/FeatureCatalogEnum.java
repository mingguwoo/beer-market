package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
public enum FeatureCatalogEnum {

    /**
     * 分类
     */
    MARKETING("Marketing"),
    ENGINEERING("Engineering");

    private final String catalog;

    FeatureCatalogEnum(String catalog) {
        this.catalog = catalog;
    }

    public String getCatalog() {
        return catalog;
    }

    public static FeatureCatalogEnum getByCatalog(String catalog) {
        return EnumUtils.getEnum(FeatureCatalogEnum.class, FeatureCatalogEnum::getCatalog, catalog);
    }

}
