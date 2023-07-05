package com.nio.ngfs.plm.bom.configuration.common.exception;

import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ConfigErrorCode configErrorCode, String dynamicMessage) {
        super(configErrorCode.getMessage() + ", " + dynamicMessage);
        this.code = configErrorCode.getCode();
        this.message = configErrorCode.getMessage();
    }


    public BusinessException(ConfigErrorCode configErrorCode) {
        super(configErrorCode.getMessage());
        this.code = configErrorCode.getCode();
        this.message = configErrorCode.getMessage();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
