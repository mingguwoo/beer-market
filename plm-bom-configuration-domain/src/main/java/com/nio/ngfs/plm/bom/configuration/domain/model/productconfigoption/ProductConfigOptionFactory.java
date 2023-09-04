package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionSelectStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
public class ProductConfigOptionFactory {

    public static ProductConfigOptionAggr create(String pcId, String optionCode, String featureCode) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, optionCode))
                .featureCode(featureCode)
                .selectStatus(ProductConfigOptionSelectStatusEnum.SELECT.getStatus())
                .selectCanEdit(CommonConstants.YES)
                .type(ProductConfigOptionTypeEnum.NORMAL.getType())
                .build();
    }

    public static ProductConfigOptionAggr createFromPc(String pcId, ProductConfigOptionAggr copyFromAggr) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, copyFromAggr.getOptionCode()))
                .featureCode(copyFromAggr.getFeatureCode())
                .selectStatus(copyFromAggr.getSelectStatus())
                .selectCanEdit(CommonConstants.YES)
                .type(ProductConfigOptionTypeEnum.FROM_PC.getType())
                .build();
    }

    public static ProductConfigOptionAggr createFromBaseVehicle(String pcId, String optionCode, String featureCode, boolean select, boolean selectCanEdit) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, optionCode))
                .featureCode(featureCode)
                .selectStatus(select ? ProductConfigOptionSelectStatusEnum.SELECT.getStatus() : ProductConfigOptionSelectStatusEnum.UNSELECT.getStatus())
                .selectCanEdit(selectCanEdit ? CommonConstants.YES : CommonConstants.NO)
                .type(ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType())
                .build();
    }

}
