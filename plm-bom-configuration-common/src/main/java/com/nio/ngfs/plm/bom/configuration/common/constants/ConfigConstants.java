package com.nio.ngfs.plm.bom.configuration.common.constants;

/**
 * 配置管理特有的常量，如果是通用常量请使用share包中的CommonConstants
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface ConfigConstants {

    String NIO = "NIO";

    ThreadLocal<String> brandName = new ThreadLocal<>();
    String GROUP_PARENT_FEATURE_CODE = "GROUP_ROOT";
    String SINGLE = "Single";
    String MAY = "May";
    String VERSION_A = "A";
    String IN_WORK = "In Work";
    String FEATURE_ATTRIBUTE_FEATURE_CODE = "Feature Code";
    String FEATURE_ATTRIBUTE_GROUP = "Group";
    String FEATURE_ATTRIBUTE_DISPLAY_NAME = "Display Name";
    String FEATURE_ATTRIBUTE_CHINESE = "Chinese Name";
    String FEATURE_ATTRIBUTE_DESCRIPTION = "Description";
    String FEATURE_ATTRIBUTE_CATALOG = "Catalog";
    String FEATURE_ATTRIBUTE_REQUESTOR = "Requestor";
    String FEATURE_ATTRIBUTE_STATUS = "Status";

    String FEATURE_OPTION_SYNC_ADD = "add";
    String FEATURE_OPTION_SYNC_UPDATE = "update";
    String FEATURE_OPTION_SYNC_CHANGE_OLD_DATA = "changeOldData";

    String FEATURE_CODE_AF00 = "AF00";

    String CONFIG_ADMIN = "config_admin";

    String CONFIG_USER = "config_user";

    String FEATURE_LIBRARY = "feature_library_admin";

    String WORKING = "Working";

    String OXO_EMAIL_GROUP = "Engineering Config.OXO";

    String OXO_EMAIL_GROUP_ALPS = "Engineering Config. OXO-Alps";

    String BASE_VEHICLE_ID_PREFIX = "BV";

}
