package com.nio.ngfs.plm.bom.configuration.domain.service.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.GetBaseVehicleOptionsRespDto;

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
     * @param updateUser     更新人
     */
    void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum, String updateUser);

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
     * 校验Group Code
     *
     * @param groupCode Group Code
     * @param isAdd     是否新增
     */
    void checkGroupCodeExistInGroupLibrary(String groupCode, boolean isAdd);

    /**
     * 将Option Code List分类成Region Option Code, Sales Version, Drive Hand三类
     */
    GetBaseVehicleOptionsRespDto sortBaseVehicleOptions(List<FeatureAggr> featureAggrList);
}
