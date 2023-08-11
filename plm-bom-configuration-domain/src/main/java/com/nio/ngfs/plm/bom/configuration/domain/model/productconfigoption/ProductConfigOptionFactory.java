package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.constants.CommonConstants;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
public class ProductConfigOptionFactory {

    public static ProductConfigOptionAggr create(String pcId, ProductConfigOptionAggr copyFromAggr) {
        return ProductConfigOptionAggr.builder()
                .productConfigOptionId(new ProductConfigOptionId(pcId, copyFromAggr.getOptionCode()))
                .selectStatus(copyFromAggr.getSelectStatus())
                .selectCanEdit(CommonConstants.YES)
                .build();
    }

}
