package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums;

import com.nio.bom.share.utils.EnumUtils;
/**
 * @author bill.wang
 * @date 2023/8/14
 */
public enum ProductContextFeatureEnum {

    /**
     * Product Context Feature的type属性
     */
    feature("Feature"),
    option("Option");

    private final String type;

    public String getType(){
        return type;
    }

    public static ProductContextFeatureEnum getByType(String type) {
        return EnumUtils.getEnum(ProductContextFeatureEnum.class, ProductContextFeatureEnum::getType, type);
    }

    ProductContextFeatureEnum(String type){this.type = type;}

}
