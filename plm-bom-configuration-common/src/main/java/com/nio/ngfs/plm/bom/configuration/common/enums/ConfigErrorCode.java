package com.nio.ngfs.plm.bom.configuration.common.enums;

import com.nio.bom.share.enums.ErrorCode;

/**
 * 配置管理特有的错误，如果是通用错误请使用share包中的CommonErrorCode
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum ConfigErrorCode implements ErrorCode {

    /**
     * 错误码
     */
    LOCK_FAILED(50000, "The Operation Failed. Please Try Again Later!"),

    DATA_NOT_EXISTS(70000, "Data Not Existed!"),

    FEATURE_GROUP_CODE_REPEAT(80000, "The Group Is Already Existed，Please Input A New One!"),
    FEATURE_GROUP_CODE_FORMAT_ERROR(80001, "Group Code Must Only Contains Alphabet Number And Blank!"),
    FEATURE_GROUP_NOT_EXISTS(80002, "Group Not Exists!"),
    FEATURE_TYPE_NOT_MATCH(80003, "Type Not Match!"),
    FEATURE_EDIT_GROUP_FEATURE_EXISTS_ACTIVE(80099, "The Group Can Not Be Inactive，Because The Following Features Have \"Active\" Status!"),
    ;

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
