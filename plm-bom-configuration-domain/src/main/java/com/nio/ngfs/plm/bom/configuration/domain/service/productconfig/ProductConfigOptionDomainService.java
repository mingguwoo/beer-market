package com.nio.ngfs.plm.bom.configuration.domain.service.productconfig;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject.BasedOnBaseVehicleFeature;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
public interface ProductConfigOptionDomainService {

    /**
     * copy From Base Vehicle的Code勾选
     *
     * @param pcId                   PC Id
     * @param baseVehicleFeatureList Base Vehicle Feature列表
     * @return ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> copyFromBaseVehicle(String pcId, List<BasedOnBaseVehicleFeature> baseVehicleFeatureList);

}
