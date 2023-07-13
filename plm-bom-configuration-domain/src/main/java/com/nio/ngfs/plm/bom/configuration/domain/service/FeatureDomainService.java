package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLog;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;

import java.util.List;

/**
 * Feature领域服务
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureDomainService {

    /**
     * 获取并校验聚合根
     *
     * @param featureCode FeatureCode
     * @param typeEnum    类型
     * @return 聚合根
     */
    FeatureAggr getAndCheckFeatureAggr(String featureCode, FeatureTypeEnum typeEnum);

    /**
     * 检查Group Code是否唯一
     *
     * @param featureAggr 聚合根
     */
    void checkGroupCodeUnique(FeatureAggr featureAggr);

    /**
     * 变更Group下的Group/Feature/Option的状态
     *
     * @param featureAggr    聚合根
     * @param changeTypeEnum Feature状态变更类型
     */
    void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum);

    /**
     * 校验Feature/Option Code是否唯一
     *
     * @param featureAggr 聚合根
     */
    void checkFeatureOptionCodeUnique(FeatureAggr featureAggr);

    /**
     * 校验Display Name是否唯一
     *
     * @param featureAggr 聚合根
     */
    void checkDisplayNameUnique(FeatureAggr featureAggr);

    /**
     * 改变Feature的Group Code
     *
     * @param featureAggr  聚合根
     * @param newGroupCode 新的Group Code
     */
    void changeFeatureGroupCode(FeatureAggr featureAggr, String newGroupCode);

    /**
     * 保存Feature变更记录
     *
     * @param featureChangeLogList Feature变更记录列表
     */
    void saveFeatureChangeLog(List<FeatureChangeLog> featureChangeLogList);

}
