package com.sh.beer.market.domain.model.configurationrule;


import com.sh.beer.market.domain.model.Repository;

/**
 * @author
 * @date 2023/10/17
 */
public interface ConfigurationRuleRepository extends Repository<ConfigurationRuleAggr, Long> {

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     *//*
    void batchSave(List<ConfigurationRuleAggr> aggrList);

    *//**
     * 批量删除
     *
     * @param aggrList 聚合根列表
     *//*
    void batchRemove(List<ConfigurationRuleAggr> aggrList);


    *//**
     * 批量更新
     * @param aggrList
     *//*
    void batchUpdate(List<ConfigurationRuleAggr> aggrList);


    *//**
     * 根据Group Id查询
     *
     * @param groupId Group Id
     * @return 聚合根列表
     *//*
    List<ConfigurationRuleAggr> queryByGroupId(Long groupId);


    *//**
     * 根据status查询
     * @param status
     * @return
     *//*
    List<ConfigurationRuleAggr> queryByIdsAndStatus(List<Long> groupIds,String status,boolean optionFlag);

    *//**
     * 根据Group Id列表批量查询
     *
     * @param groupIdList Group Id列表
     * @return 聚合根列表
     *//*
    List<ConfigurationRuleAggr> queryByGroupIdList(List<Long> groupIdList);

    *//**
     * 根据Rule Id列表批量查询
     *
     * @param ruleIdList Rule Id列表
     * @return 聚合根列表
     *//*
    List<ConfigurationRuleAggr> queryByRuleIdList(List<Long> ruleIdList, boolean optionFlag);


    *//**
     * 根据rule_number批量查询
     * @param ruleNumbers
     * @return
     *//*
    List<ConfigurationRuleAggr> queryByRuleNumbers(List<String> ruleNumbers);

    *//**
     * 申请Rule Number
     *
     * @param size 申请数量
     * @return Rule Number列表
     *//*
    List<String> applyRuleNumber(int size);*/

}
