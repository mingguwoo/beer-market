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
    DATA_NOT_EXISTS(50001, "Data Not Existed!"),
    CLONE_ERROR(50002, "Clone Error!"),
    EXCEL_DOWNLOAD_ERROR(50003, "Excel Download Error!"),
    EXCEL_UPLOAD_ERROR(50004, "Excel Upload Error!"),

    /**
     * Feature Library
     */
    FEATURE_GROUP_CODE_REPEAT(80000, "The Group Is Already Existed，Please Input A New One!"),
    FEATURE_GROUP_CODE_FORMAT_ERROR(80001, "Group Code Must Only Contains Alphabet Number Blank And -!"),
    FEATURE_GROUP_NOT_EXISTS(80002, "Group Not Existed!"),
    FEATURE_TYPE_NOT_MATCH(80003, "Type Not Match!"),
    FEATURE_STATUS_INVALID(80004, "Status Is Invalid!"),
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
    FEATURE_OPTION_CHINESE_NAME_REPEAT(80015, "The Chinese Name Is Already Existed, Please Input A New One!"),
    FEATURE_OPTION_CODE_DIFF_FROM_FEATURE_CODE(80016, "The Top Two of The Option Must Be The Same With Feature!"),
    FEATURE_OPTION_NOT_EXISTS(80017, "Option Not Existed!"),
    FEATURE_GROUP_ROOT_IS_INTERNAL(80018, "GROUP_ROOT Is Used For Internal!"),
    FEATURE_ADD_GROUP_IN_3DE_FIRST(80019, "Please Add This Group In 3DE Group Library First!"),
    FEATURE_UPDATE_GROUP_IN_3DE_FIRST(80020, "Please Update This Group In 3DE Group Library First!"),
    FEATURE_TYPE_INVALID(80021, "Type Is Invalid!"),
    FEATURE_FEATURE_CODE_TOO_SHORT(80022, "Feature Code Min Length Is 4!"),
    FEATURE_OPTION_CODE_TOO_SHORT(80023, "Option Code Min Length Is 4!"),
    FEATURE_STATUS_NOT_ACTIVE_CAN_NOT_EDIT(80024, "The Feature Status Is Inactive, Can Not Edit!"),
    FEATURE_FEATURE_IS_NOT_ACTIVE(80025, "Feature Is Not Active!"),
    FEATURE_FEATURE_CAN_NOT_BE_ACTIVE(80026, "Group Is Inactive, Feature Can Not Be Active! "),
    FEATURE_OPTION_CAN_NOT_BE_ACTIVE(80027, "Feature Is Inactive, Option Can Not Be Active! "),

    /**
     * Base Vehicle
     */
    BASE_VEHICLE_MATURITY_INVALID(81000, "Maturity Is Invalid!"),
    BASE_VEHICLE_REPEAT(81001, "This Base Vehicle already exists, can not be created repeatedly!"),
    BASE_VEHICLE_NOT_EXISTS(81002, "Base Vehicle Not Existed!"),
    BASE_VEHICLE_MODEL_CODE_MODEL_YEAR_INVALID(81003, "Base Vehicle Can Not Change Model Code Or Model Year!"),
    BASE_VEHICLE_MATURITY_CHANGE_INVALID(81004, "Base Vehicle Can Not Revert Its Maturity From P To U!"),
    BASE_VEHICLE_STATUS_INVALID(81005, "Base Vehicle Status Is Invalid!"),
    BASE_VEHICLE_ALREADY_RELEASED(81006, "The Base Vehicle Is Published, Can Not Delete!"),

    /**
     * OXO
     */
    OXO_VERSION_SNAPSHOT_NOT_EXIST(82000, "OXO Version Snapshot Not Exist!"),
    OXO_FEATURE_OPTION_CAN_NOT_DELETE(82001, "The Feature/Option Is Already Deleted, Can Not Delete It Again!"),
    AF_ERROR(82002, "Please Add AF00 In OXO!"),
    OXO_RENEW_SORT_FEATURE_NOT_THE_SAME_GROUP(82003, "The Select Features Not All In The Same Group And Catalog!"),
    OXO_RENEW_SORT_OPTION_NOT_THE_SAME_GROUP(82004, "The Select Options Not All In The Same Feature!"),
    OXO_FEATURE_OPTION_IS_ALREADY_DELETE(82005, "Some Of The Selected Feature/Option Is Already Deleted!"),

    /**
     * Product Config
     */
    PRODUCT_CONFIG_BASED_ON_ONLY_SELECT_ONE(83000, "Based On Base Vehicle And Base On PC Can Only Select One!"),
    PRODUCT_CONFIG_PC_ID_FORMAT_ERROR(83001, "PC Id Format Error!"),
    PRODUCT_CONFIG_PC_NAME_HAS_SPACE(83002, "PC Name Has Blank, Please Remove It!"),
    PRODUCT_CONFIG_PC_NAME_FORMAT_ERROR(83003, "PC Name Can Only Contains Alphabet、Number、_、-、. !"),
    PRODUCT_CONFIG_PC_NAME_REPEAT(83004, "This PC Name Is Existed In【Model/Model Year】, Please Input A New One!"),
    PRODUCT_CONFIG_BASED_ON_PC_NOT_EXIST(83005, "Based On PC Not Exist!"),
    PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_NOT_EXIST(83006, "Based On Base Vehicle Not Exist!"),
    PRODUCT_CONFIG_OXO_VERSION_SNAPSHOT_ID_IS_NULL(83007, "OXO Version Snapshot Id Is Null!"),
    PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_TYPE_ERROR(83008, "Copy From Released OXO Base Vehicle Option Error!"),
    PRODUCT_CONFIG_PC_NOT_EXIST(83009, "PC Not Exist!"),
    PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_OPTION_NOT_EXIST(83010, "Based On Base Vehicle Option Code Not Exist!"),
    PRODUCT_CONFIG_SKIP_CHECK_CLOSE_ERROR(83011, "At Most One Option Can Select In The Same Feature When SkipCheck Is Close!"),
    PRODUCT_CONFIG_OPTION_CAN_NOT_EDIT(83012, "Product Config Can Not Edit!"),
    PRODUCT_CONFIG_OPTION_CAN_NOT_SELECT(83013, "Option xxx Is Not Applied In Product Context xxx, Which Can Not Be Applied In Related PC Either!"),
    PRODUCT_CONFIG_OPTION_AT_LEAST_SELECT_ONE(83014, "Feature xxx Is Applied In Product Context xxx, Please Choose At Least One Option Of The Feature In PC xxx!"),

    /**
     * Other
     */
    BOM_MIDDLE_PLATFORM_MODEL_NOT_EXIST(89000, "Model Not Exist In Bom Middle Platform!"),
    BOM_MIDDLE_PLATFORM_GET_MODEL_FAIL(89001, "Get Model From Bom Middle Platform Fail!"),


    EMAIL_ERROR(89002, "send email error!"),


    BASIC_VERSION_ERROR(89003, "对比版本号不能一样"),


    VERSION_ERROR(89004, "版本{0}没有数据"),


    OPTION_ERROR(89005, "Option {0} Is Not Consistent With Product Configuration【Model Year:{1}】!"),

    MATURITY_ERROR(89006, "The Maturity Of All Base Vehicle Is 'U', Can Not Publish Formal Version!"),

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
