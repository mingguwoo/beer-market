package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;

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
     * @param featureId 唯一id
     * @param errorCode 错误码
     * @return 聚合根
     */
    FeatureAggr getAndCheckFeatureAggr(FeatureId featureId, ConfigErrorCode errorCode);

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
     * Chinese Name在同一feature下是否唯一
     *
     * @param featureAggr 聚合根
     */
    void checkOptionChineseNameUnique(FeatureAggr featureAggr);

    /**
     * 校验OptionCode前两位与所属Feature是否一致
     *
     * @param featureAggr 聚合根
     */
    void checkOptionCodeAndFeatureCodeTwoDigits(FeatureAggr featureAggr);
}
