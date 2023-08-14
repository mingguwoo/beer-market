package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
public enum BasedOnBaseVehicleTypeEnum {

    /**
     * Based On Base Vehicle类型
     */
    // 全部实心圆
    ALL_Default,
    // 全部实心圆和-
    ALL_Default_AND_Unavailable,
    // 全部-
    ALL_Unavailable,
    // 存在空心圆
    EXIST_Available

}
