package com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ModelYearConfigFactory {

    public ModelYearConfigAggr create(String model, String modelYear) {
        return ModelYearConfigAggr.builder()
                .modelYearConfigId(new ModelYearConfigId(model, modelYear))
                .build();
    }

}
