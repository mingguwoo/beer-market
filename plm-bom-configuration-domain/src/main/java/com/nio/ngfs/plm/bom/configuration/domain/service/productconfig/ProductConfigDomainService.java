package com.nio.ngfs.plm.bom.configuration.domain.service.productconfig;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ProductConfigDomainService {

    /**
     * 生成PC Id
     *
     * @param model     车型
     * @param modelYear Model Year
     * @return PC Id
     */
    String generatePcId(String model, String modelYear);

    /**
     * 校验PC Name是否唯一
     *
     * @param productConfigAggr 聚合根
     */
    void checkPcNameUnique(ProductConfigAggr productConfigAggr);

}
