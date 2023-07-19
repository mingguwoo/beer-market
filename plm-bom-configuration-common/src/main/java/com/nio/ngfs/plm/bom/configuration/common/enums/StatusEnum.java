package com.nio.ngfs.plm.bom.configuration.common.enums;

import com.nio.bom.share.utils.EnumUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum StatusEnum {

    /**
     * 状态
     */
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StatusEnum getByStatus(String status) {
        return EnumUtils.getEnum(StatusEnum.class, StatusEnum::getStatus, status);
    }

}
