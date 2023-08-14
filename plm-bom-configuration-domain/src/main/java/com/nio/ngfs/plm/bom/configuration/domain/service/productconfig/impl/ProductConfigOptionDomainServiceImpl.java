package com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.impl;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject.BasedOnBaseVehicleFeature;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.BasedOnBaseVehicleTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigOptionDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.BasedOnBaseVehicleTypeEnum.*;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
@Service
@RequiredArgsConstructor
public class ProductConfigOptionDomainServiceImpl implements ProductConfigOptionDomainService {

    @Override
    public List<ProductConfigOptionAggr> copyFromBaseVehicle(String pcId, List<BasedOnBaseVehicleFeature> baseVehicleFeatureList) {
        if (CollectionUtils.isEmpty(baseVehicleFeatureList)) {
            return Collections.emptyList();
        }
        List<ProductConfigOptionAggr> productConfigOptionAggrList = Lists.newArrayList();
        baseVehicleFeatureList.forEach(baseVehicleFeature -> {
            BasedOnBaseVehicleTypeEnum basedOnBaseVehicleTypeEnum = baseVehicleFeature.getBasedOnBaseVehicleType();
            if (ALL_Default == basedOnBaseVehicleTypeEnum) {
                baseVehicleFeature.getOptionList().forEach(option -> {
                    // 所有Option自动勾选，不可编辑
                    productConfigOptionAggrList.add(ProductConfigOptionFactory.createFromBaseVehicle(pcId, option.getOptionCode(), true, false));
                });
            } else if (ALL_Default_AND_Unavailable == basedOnBaseVehicleTypeEnum) {
                baseVehicleFeature.getOptionList().forEach(option -> {
                    // 实心圆的Option自动勾选，不可编辑
                    productConfigOptionAggrList.add(ProductConfigOptionFactory.createFromBaseVehicle(pcId, option.getOptionCode(), option.isDefault(), false));
                });
            } else if (ALL_Unavailable == basedOnBaseVehicleTypeEnum) {
                baseVehicleFeature.getOptionList().forEach(option -> {
                    // 所有Option都不勾选，不可编辑
                    productConfigOptionAggrList.add(ProductConfigOptionFactory.createFromBaseVehicle(pcId, option.getOptionCode(), false, false));
                });
            } else if (EXIST_Available == basedOnBaseVehicleTypeEnum) {
                baseVehicleFeature.getOptionList().forEach(option -> {
                    // 所有Option都不勾选，仅实心圆和空心圆的Option可编辑
                    productConfigOptionAggrList.add(ProductConfigOptionFactory.createFromBaseVehicle(pcId, option.getOptionCode(), false, option.isDefault() || option.isAvailable()));
                });
            }
        });
        return productConfigOptionAggrList;
    }

}
