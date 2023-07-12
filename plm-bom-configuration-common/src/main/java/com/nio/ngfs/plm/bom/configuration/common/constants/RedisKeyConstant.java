package com.nio.ngfs.plm.bom.configuration.common.constants;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
public interface RedisKeyConstant {

    String CONFIGURATION_PREFIX = "configuration:";

    String FEATURE_GROUP_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "feature:group:lock:";

    String OPTION_FEATURE_LOCK_KEY_PREFIX = CONFIGURATION_PREFIX + "option:feature:lock:";

}
