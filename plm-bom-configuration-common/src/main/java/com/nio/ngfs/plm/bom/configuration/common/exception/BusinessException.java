package com.nio.ngfs.plm.bom.configuration.common.exception;

import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode, String dynamicMessage) {
        super(errorCode.getMessage() + ", " + dynamicMessage);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
