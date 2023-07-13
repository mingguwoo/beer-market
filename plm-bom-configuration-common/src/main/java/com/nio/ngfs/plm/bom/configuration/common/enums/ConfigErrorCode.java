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

    /**
     * Feature Library
     */
    FEATURE_GROUP_CODE_REPEAT(80000, "The Group Is Already Existed，Please Input A New One!"),
    FEATURE_GROUP_CODE_FORMAT_ERROR(80001, "Group Code Must Only Contains Alphabet Number And Blank!"),
    FEATURE_GROUP_NOT_EXISTS(80002, "Group Not Existed!"),
    FEATURE_TYPE_NOT_MATCH(80003, "Type Not Match!"),
    FEATURE_STATUS_INVALID(80004, "Feature Status Is Invalid!"),
    FEATURE_CHANGE_GROUP_STATUS_FEATURE_EXIST_ACTIVE(80005, "The Group Can Not Be Inactive，Because The Following Features Have \"Active\" Status!"),
    FEATURE_FEATURE_CODE_FORMAT_ERROR(80006, "Feature Code Must Only Contains Alphabet And Number!"),
    FEATURE_OPTION_CODE_FORMAT_ERROR(80007, "Option Code Must Only Contains Alphabet And Number!"),
    FEATURE_CATALOG_INVALID(80008, "Catalog Is Invalid!"),
    FEATURE_REQUESTOR_INVALID(80009, "Requestor Is Invalid!"),
    FEATURE_GROUP_IS_NOT_ACTIVE(80010, "Group Is Not Active!"),
    FEATURE_FEATURE_CODE_REPEAT(80011, "The Feature Is Already Existed，Please Input A New One!"),
    FEATURE_OPTION_CODE_REPEAT(80012, "The Option Is Already Existed，Please Input A New One!"),
    FEATURE_DISPLAY_NAME_REPEAT(80013, "The Display Name Is Already Existed, Please Input A New One!"),
    FEATURE_FEATURE_NOT_EXISTS(80014, "Feature Not Existed!"),
    FEATURE_OPTION_CHINESE_NAME_REPEAT(80015, "Chinese Name Is Already Existed, Please Input A New One!"),
    FEATURE_OPTION_CODE_DIFF_FROM_FEATURE_CODE(80016, "The First Two Digits Of Option Code Is Different From Feature Code"),
    FEATURE_OPTION_NOT_EXISTS(80017, "Option Not Existed!"),
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
