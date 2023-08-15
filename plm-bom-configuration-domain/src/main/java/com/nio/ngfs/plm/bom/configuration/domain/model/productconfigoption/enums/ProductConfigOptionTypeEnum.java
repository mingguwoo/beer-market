package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
public enum ProductConfigOptionTypeEnum {

    /**
     * 类型
     */
    NORMAL(0),
    FROM_BASE_VEHICLE(1),
    FROM_PC(2);

    private final int type;

    ProductConfigOptionTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
