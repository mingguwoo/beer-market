package com.nio.ngfs.plm.bom.configuration.common.constants;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 配置管理特有的常量，如果是通用常量请使用share包中的CommonConstants
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface ConfigConstants {

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

    String V36_CODE_ATTRIBUTE_DISPLAY_NAME = "Display Name";
    String V36_CODE_ATTRIBUTE_CHINESE_NAME = "Chinese Name";
    String V36_CODE_ATTRIBUTE_SALES_FEATURE = "Sales Feature";
    String V36_CODE_ATTRIBUTE_REMARK = "Remark";

    String FEATURE_OPTION_SYNC_ADD = "add";
    String FEATURE_OPTION_SYNC_UPDATE = "update";
    String FEATURE_OPTION_SYNC_CHANGE_OLD_DATA = "changeOldData";

    String FEATURE_CODE_AF00 = "AF00";

    String CONFIG_ADMIN = "config_admin";

    String CONFIG_USER = "config_user";

    String FEATURE_LIBRARY = "feature_library_admin";

    String WORKING = "Working";

    String OXO_EMAIL_GROUP = "Engineering Config.OXO";

    String OXO_EMAIL_GROUP_ALPS = "Engineering Config.OXO-Alps";

    String BASE_VEHICLE_ID_PREFIX = "BV";

    String BASE_VEHICLE_REGION_FEATURE = "AD00";
    String BASE_VEHICLE_DRIVE_HAND_FEATURE = "AN00";
    String BASE_VEHICLE_SALES_VERSION_FEATURE = "19AA";
    List<String> BASE_VEHICLE_FEATURE_CODE_LIST = Lists.newArrayList(BASE_VEHICLE_REGION_FEATURE, BASE_VEHICLE_DRIVE_HAND_FEATURE, BASE_VEHICLE_SALES_VERSION_FEATURE);

    long V36_CODE_DIGIT_PARENT_CODE_ID = -1;
    String V36_CODE_DIGIT_PARENT_CODE = "V36_ROOT";

    int OXO_FEATURE_OPTION_SORT_DEFAULT = 1000;

    String HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    String HEADER_CONTENT_DISPOSITION = "Content-Disposition";


    String REG_DOT = ".";


    String SPLIT_DOT = "\\.";


    String VERSION_AA = "AA";

    String VERSION_AA_1 = "AA.1";


    String BASE_VERSION = "baseVersion";


    String UN_AVAILABLE = "Unavailable";


    String LOW_VERSION = "lowVersion";


    String PRODUCT_CONFIGURATION_ERROR = "Feature {0} Is Applied In Product Configuration!";

    String OXO_EMAIL_GROUP_FY = "Engineering Config.OXO-Fy";
}
