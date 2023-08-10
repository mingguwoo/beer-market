package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ProductConfigApplicationService {

    /**
     * copy PC的Option Code勾选
     *
     * @param productConfigAggr PC聚合根
     */
    void copyProductConfigOptionByPc(ProductConfigAggr productConfigAggr);

}
