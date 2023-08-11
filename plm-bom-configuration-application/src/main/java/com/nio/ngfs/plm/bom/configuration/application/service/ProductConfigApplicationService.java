package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ProductConfigApplicationService {

    /**
     * copy PC的Option Code勾选
     *
     * @param productConfigAggr PC聚合根
     * @return ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> copyProductConfigOptionByPc(ProductConfigAggr productConfigAggr);

    /**
     * copy Base Vehicle的Option Code勾选
     *
     * @param productConfigAggr PC聚合根
     * @return ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> copyProductConfigOptionByBaseVehicle(ProductConfigAggr productConfigAggr);

}
