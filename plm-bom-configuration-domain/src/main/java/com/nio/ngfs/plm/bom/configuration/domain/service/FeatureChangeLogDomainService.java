package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
public interface FeatureChangeLogDomainService {

    /**
     * 构建Group/Feature/Option的Status变更记录
     *
     * @param event event
     * @return FeatureChangeLogAggr列表
     */
    List<FeatureChangeLogAggr> buildStatusChangeLogByGroupFeatureAndOption(FeatureStatusChangeEvent event);

    /**
     * 构建Feature属性变更记录
     *
     * @param event Feature属性变更事件
     * @return FeatureChangeLogAggr列表
     */
    List<FeatureChangeLogAggr> buildFeatureAttributeChangeLog(FeatureAttributeChangeEvent event);

}
