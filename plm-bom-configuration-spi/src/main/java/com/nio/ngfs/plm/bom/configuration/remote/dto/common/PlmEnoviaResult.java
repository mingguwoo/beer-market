package com.nio.ngfs.plm.bom.configuration.remote.dto.common;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Data
public class PlmEnoviaResult<T> {

    private String code;

    private boolean status;

    private String msg;

    private T data;

    public boolean isSuccess() {
        return status;
    }

}
