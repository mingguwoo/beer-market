package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface BomsConfigurationRuleDao extends IService<BomsConfigurationRuleEntity> {

    /**
     * 获取最大的Rule Number
     *
     * @return Rule Number
     */
    String getMaxRuleNumber();

    /**
     * 根据Group Id查询
     *
     * @param groupId Group Id
     * @return BomsConfigurationRuleEntity列表
     */
    List<BomsConfigurationRuleEntity> queryByGroupId(Long groupId);

    /**
     * 根据Group Id列表批量查询
     *
     * @param groupIdList Group Id列表
     * @return BomsConfigurationRuleEntity列表
     */
    List<BomsConfigurationRuleEntity> queryByGroupIdList(List<Long> groupIdList);

    List<BomsConfigurationRuleEntity> queryByRuleNumber(String ruleNumber);

    /**
     * 根据groupId的List来查询Configuration Rule
     * @param groupId
     * @return
     */
    List<BomsConfigurationRuleEntity> queryByGroupId(List<Long> groupId);
}
