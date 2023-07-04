package com.nio.ngfs.plm.bom.configuration.common.enums;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public enum ErrorCode {

    /**
     * 成功
     */
    SUCCESS("200", "成功"),


    /**
     * 服务端处理失败
     */
    SERVER_ERROR("50001", "服务端处理失败"),

    /**
     * 第三方服务异常
     */
    THIRD_PARTY_ERROR("50002", "第三方服务异常"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR("50003", "参数错误"),

    /**
     * 没有登陆
     */
    NOT_LOGIN("50004", "没有登陆"),

    /**
     * 没有登陆
     */
    PERMISSION_DENY("50005", "没有权限"),
    /**
     *
     */
    GET_USER_PERMISSION_ERROR("50006", "获取用户权限失败"),

    /**
     * 消息消费失败
     */
    CONSUMER_ERROR("60000", "消息消费失败"),

    /**
     * Check json 失败，不需要重试的错误
     */
    CHECK_ERROR("70000", "json校验失败"),

    FEATURE_ADD_GROUP_GROUP_CODE_REPEAT("80000", "The Group Is Already Existed，Please Input A New One!"),
    FEATURE_NOT_EXISTS("80001", "Feature not exists!"),
    FEATURE_EDIT_GROUP_FEATURE_EXISTS_ACTIVE("80000", "The Group Can Not Be Inactive，Because The Following Features Have \"Active\" Status!"),

    ;


    /**
     * 错误码编号
     */
    private final String code;

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
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
