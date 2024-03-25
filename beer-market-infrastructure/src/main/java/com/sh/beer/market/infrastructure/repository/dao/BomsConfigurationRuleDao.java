package com.sh.beer.market.infrastructure.repository.dao;

import com.baomidou.mybatisplus.service.IService;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleEntity;

/**
 * @author
 * @date 2023/10/17
 */
public interface BomsConfigurationRuleDao extends IService<BomsConfigurationRuleEntity> {

    /**
     * 获取最大的Rule Number
     *
     * @return Rule Number
     *//*
    String getMaxRuleNumber();

    *//**
     * 根据Group Id查询
     *
     * @param groupId Group Id
     * @return BomsConfigurationRuleEntity列表
     *//*
    List<BomsConfigurationRuleEntity> queryByGroupId(Long groupId);


    *//**
     * 根据status查询
     * @param status
     * @return
     *//*
    List<BomsConfigurationRuleEntity> queryByGroupIdsAndStatus(List<Long> groupIds, String status);

    *//**
     * 根据Group Id列表批量查询
     *
     * @param groupIdList Group Id列表
     * @return BomsConfigurationRuleEntity列表
     *//*
    List<BomsConfigurationRuleEntity> queryByGroupIdList(List<Long> groupIdList);


    *//**
     * 根据rule number批量查询
     * @param ruleNumbers
     * @return
     *//*
    List<BomsConfigurationRuleEntity> queryByRuleNumbers(List<String> ruleNumbers);
*/
}
