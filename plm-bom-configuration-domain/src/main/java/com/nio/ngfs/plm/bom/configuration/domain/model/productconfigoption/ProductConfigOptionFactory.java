package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
public class ProductConfigOptionFactory {

    public static ProductConfigOptionAggr create(Long pcId, String optionCode, String featureCode, boolean select) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, optionCode))
                .featureCode(featureCode)
                .selectStatus(select ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode())
                .selectCanEdit(YesOrNoEnum.YES.getCode())
                .type(ProductConfigOptionTypeEnum.NORMAL.getType())
                .build();
    }

    public static ProductConfigOptionAggr createFromPc(Long pcId, ProductConfigOptionAggr copyFromAggr) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, copyFromAggr.getOptionCode()))
                .featureCode(copyFromAggr.getFeatureCode())
                .selectStatus(copyFromAggr.getSelectStatus())
                .selectCanEdit(YesOrNoEnum.YES.getCode())
                .type(ProductConfigOptionTypeEnum.FROM_PC.getType())
                .build();
    }

    public static ProductConfigOptionAggr createFromBaseVehicle(Long pcId, String optionCode, String featureCode, boolean select, boolean selectCanEdit) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, optionCode))
                .featureCode(featureCode)
                .selectStatus(select ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode())
                .selectCanEdit(selectCanEdit ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode())
                .type(ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType())
                .build();
    }

}
