package com.nio.ngfs.plm.bom.configuration.common.enums;

import com.nio.bom.share.enums.ErrorCode;

/**
 * 配置管理特有的错误，如果是通用错误请使用share包中的CommonErrorCode
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum ConfigErrorCode implements ErrorCode {

    FEATURE_ADD_GROUP_GROUP_CODE_REPEAT(80000, "The Group Is Already Existed，Please Input A New One!"),
    FEATURE_NOT_EXISTS(80001, "Feature not exists!"),
    FEATURE_EDIT_GROUP_FEATURE_EXISTS_ACTIVE(80000, "The Group Can Not Be Inactive，Because The Following Features Have \"Active\" Status!");

    /**
     * 错误码编号
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    返回码编号
     * @param message 返回消息
     */
    ConfigErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
