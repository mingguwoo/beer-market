package com.sh.beer.market.infrastructure.repository.dao;

import com.baomidou.mybatisplus.service.IService;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;

import java.util.List;

/**
 * @author
 * @date 2023/10/17
 */
public interface BomsConfigurationRuleOptionDao extends IService<BomsConfigurationRuleOptionEntity> {

    /**
     * 根据ruleId查询
     *
     * @param ruleId ruleId
     * @return BomsConfigurationRuleOptionEntity列表
     *//*
    List<BomsConfigurationRuleOptionEntity> queryByRuleId(Long ruleId);

    *//**
     * 根据ruleId列表批量查询
     *
     * @param ruleIdList ruleId列表
     * @return BomsConfigurationRuleOptionEntity列表
     *//*
    List<BomsConfigurationRuleOptionEntity> queryByRuleIdList(List<Long> ruleIdList);
*/
}
