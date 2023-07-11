package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;

import java.util.List;

/**
 * Feature领域服务
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureDomainService {

    /**
     * check并获取聚合根
     *
     * @param featureId 唯一id
     * @param message   错误信息
     * @return 聚合根
     */
    FeatureAggr getAndCheckFeatureAggr(FeatureId featureId, String message);

    /**
     * 检查Group Code是否唯一
     *
     * @param featureAggr featureAggr
     */
    void checkGroupCodeUnique(FeatureAggr featureAggr);

    /**
     * Group下的Feature/Option状态改为Active
     *
     * @param featureAggr    聚合根
     * @param changeTypeEnum Feature状态变更类型
     */
    void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum);

}
