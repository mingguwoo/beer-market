package com.nio.ngfs.plm.bom.configuration.domain.model.v36code.common;

import com.google.common.collect.Maps;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
public class V36CodeLibraryAggrThreadLocal {

    public static final ThreadLocal<Map<Long, V36CodeLibraryAggr>> LOCAL = ThreadLocal.withInitial(Maps::newHashMap);

    /**
     * 获取聚合根集合
     */
    public static V36CodeLibraryAggr get(Long id) {
        return LOCAL.get().get(id);
    }

    /**
     * 添加聚合根
     */
    public static void add(V36CodeLibraryAggr aggr) {
        // copy一份数据
        V36CodeLibraryAggr copyAggr = new V36CodeLibraryAggr();
        BeanUtils.copyProperties(aggr, copyAggr);
        LOCAL.get().put(copyAggr.getId(), copyAggr);
    }

    /**
     * 清除
     */
    public static void remove() {
        LOCAL.remove();
    }

}
