package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum FeatureStatusEnum {

    /**
     * Feature状态
     */
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;

    FeatureStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
