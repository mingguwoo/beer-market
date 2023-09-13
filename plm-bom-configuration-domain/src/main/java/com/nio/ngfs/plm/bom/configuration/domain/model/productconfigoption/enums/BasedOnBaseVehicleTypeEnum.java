package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
public enum BasedOnBaseVehicleTypeEnum {

    /**
     * Based On Base Vehicle类型
     */
    // 仅有一个实心圆
    ONLY_Default,
    // 仅有一个一个实心圆和多个-
    ONLY_Default_AND_Unavailable,
    // 全部-
    ALL_Unavailable,
    // 存在空心圆
    EXIST_Available

}
