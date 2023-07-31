package com.nio.ngfs.plm.bom.configuration.common.constants;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
public interface RedisKeyConstant {

    String CONFIGURATION_PREFIX = "configuration:";

    /**
     * Feature Library的Group锁
     */
    String FEATURE_GROUP_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:group:lock:";

    /**
     * Feature Library的Feature锁
     */
    String FEATURE_FEATURE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:feature:lock:";

    String FEATURE_OPTION_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:option:lock:";


    String MODEL_FEATURE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "modelCode:feature:lock:";


    String MODEL_OXO_FEATURE_EDIT_PREFIX = CONFIGURATION_PREFIX + "modelCode:oxo:feature:edit:lock:";



    /**
     * Base Vehicle的流水号
     */
    String BASE_VEHICLE_ID_KEY = CONFIGURATION_PREFIX + "baseVehicle:id";

    String OXO_FEATURE_OPTION_DELETE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "oxo:featureOption:delete:lock:";

}
