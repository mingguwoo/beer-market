package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
public enum ProductConfigOptionSelectStatusEnum {

    /**
     * Code勾选状态
     */
    SELECT("Select"),
    UNSELECT("Unselect"),
    ;

    private final String status;

    ProductConfigOptionSelectStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
