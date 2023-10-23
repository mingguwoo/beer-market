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


    List<BomsConfigurationRuleEntity> queryByRuleNumber(String ruleNumber);
}
