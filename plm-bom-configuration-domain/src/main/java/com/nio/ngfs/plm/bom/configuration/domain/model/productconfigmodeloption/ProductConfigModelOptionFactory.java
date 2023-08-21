package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
public class ProductConfigModelOptionFactory {

    public static ProductConfigModelOptionAggr create(String modelCode, String optionCode, String featureCode) {
        return ProductConfigModelOptionAggr.builder()
                .productConfigModelOptionId(new ProductConfigModelOptionId(modelCode, optionCode))
                .featureCode(featureCode)
                .build();
    }

}
