package com.nio.ngfs.plm.bom.configuration.common.constants;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
public interface RedisKeyConstant {

    String CONFIGURATION_PREFIX = "configuration:";

    /**
     * Feature Library
     */
    String FEATURE_GROUP_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:group:lock:";
    String FEATURE_FEATURE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:feature:lock:";
    String FEATURE_OPTION_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:option:lock:";
    String MODEL_FEATURE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "modelCode:feature:lock:";
    String MODEL_OXO_FEATURE_EDIT_PREFIX = CONFIGURATION_PREFIX + "modelCode:oxo:feature:edit:lock:";
    String FEATURE_FULL_SYNC_TO_ENOVIA = CONFIGURATION_PREFIX + "fullSyncToEnovia:lock";

    /**
     * Base Vehicle
     */
    String BASE_VEHICLE_ID_KEY = CONFIGURATION_PREFIX + "baseVehicle:id";
    String OXO_FEATURE_OPTION_DELETE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "oxo:featureOption:delete:lock:";
    String OXO_SAVE_SNAPSHOT_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "oxo:save:snapshot:lock:";

    /**
     * Product Config
     */
    String PRODUCT_CONFIG_ADD_PC_LOCK_KEY_FORMAT = CONFIGURATION_PREFIX + "productConfig:pc:add:lock:%s:%s";
    String PRODUCT_CONFIG_EDIT_PC_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "productConfig:pc:edit:lock:";
    String PRODUCT_CONFIG_DELETE_PC_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "productConfig:pc:delete:lock:";
    String PRODUCT_CONFIG_EDIT_PRODUCT_CONFIG_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "productConfig:productConfig:edit:lock:";
    String PRODUCT_CONFIG_SYNC_TO_ENOVIA_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "syncPcToEnovia:lock:";

    /**
     * V36 Code
     */
    String V36_CODE_DIGIT_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "v36Code:digit:lock:";
    String V36_CODE_OPTION_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "v36Code:option:lock:";

    /**
     * Configuration Rule
     */
    String CONFIGURATION_RULE_RULE_NUMBER_INCR_LOCK = CONFIGURATION_PREFIX + "configurationRule:ruleNumber:incr:lock";
    String CONFIGURATION_RULE_GROUP_LOCK = CONFIGURATION_PREFIX + "configurationRule:group:lock:";
    String CONFIGURATION_RULE_RULE_RELEASE_LOCK = CONFIGURATION_PREFIX + "configurationRule:rule:release:lock";

}
