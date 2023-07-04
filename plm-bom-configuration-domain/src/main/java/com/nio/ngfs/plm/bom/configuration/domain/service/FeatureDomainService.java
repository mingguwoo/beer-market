package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;

/**
 * Feature领域服务
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureDomainService {

    /**
     * check并获取集合根
     *
     * @param id               主键ID
     * @param containsChildren 是否包含children
     * @return 集合根
     */
    FeatureAggr checkAndGetFeatureAggr(Long id, boolean containsChildren);

    /**
     * 检查Group Code是否唯一
     *
     * @param featureAggr featureAggr
     */
    void checkGroupCodeUnique(FeatureAggr featureAggr);

}
