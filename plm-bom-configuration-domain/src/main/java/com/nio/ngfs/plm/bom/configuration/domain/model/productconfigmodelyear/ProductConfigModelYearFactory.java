package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ProductConfigModelYearFactory {

    public static ProductConfigModelYearAggr create(String model, String modelYear) {
        return ProductConfigModelYearAggr.builder()
                .productConfigModelYearId(new ProductConfigModelYearId(model, modelYear))
                .build();
    }

}
