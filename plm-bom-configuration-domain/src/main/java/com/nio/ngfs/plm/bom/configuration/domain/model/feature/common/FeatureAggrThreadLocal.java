package com.nio.ngfs.plm.bom.configuration.domain.model.feature.common;

import com.google.common.collect.Maps;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;

import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
public class FeatureAggrThreadLocal {

    public static final ThreadLocal<Map<Long, FeatureAggr>> LOCAL = ThreadLocal.withInitial(Maps::newHashMap);

    /**
     * 获取聚合根集合
     */
    public static Map<Long, FeatureAggr> get() {
        return LOCAL.get();
    }

    /**
     * 添加聚合根
     */
    public static void add(FeatureAggr aggr) {
        // copy一份数据
        FeatureAggr copyAggr = aggr.clone();
        get().put(copyAggr.getId(), copyAggr);
    }

    /**
     * 清除
     */
    public static void remove() {
        LOCAL.remove();
    }

}
