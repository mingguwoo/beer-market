package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.common.enums.StatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;

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
    void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, StatusChangeTypeEnum changeTypeEnum, String updateUser);

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
     * 构建Option的Group变更记录
     *
     * @param event      GroupCode变更事件
     * @param optionList Option列表
     * @return FeatureChangeLogDo列表
     */
    List<FeatureChangeLogDo> buildGroupChangeLogByOption(GroupCodeChangeEvent event, List<FeatureAggr> optionList);

    /**
     * 构建Group/Feature/Option的Status变更记录
     *
     * @param event event
     * @return FeatureChangeLogDo列表
     */
    List<FeatureChangeLogDo> buildStatusChangeLogByGroupFeatureAndOption(FeatureStatusChangeEvent event);

    /**
     * 构建Feature属性变更记录
     *
     * @param event Feature属性变更事件
     * @return FeatureChangeLogDo列表
     */
    List<FeatureChangeLogDo> buildFeatureAttributeChangeLog(FeatureAttributeChangeEvent event);

    /**
     * 校验Group Code
     *
     * @param groupCode Group Code
     * @param isAdd     是否新增
     */
    void checkGroupCodeExistInGroupLibrary(String groupCode, boolean isAdd);

}
