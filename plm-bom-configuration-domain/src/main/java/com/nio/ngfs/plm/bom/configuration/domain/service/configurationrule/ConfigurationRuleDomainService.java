package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleDomainService {

    /**
     * 获取并校验聚合根
     *
     * @param id id
     * @return 聚合根
     */
    ConfigurationRuleAggr getAndCheckAggr(Long id);

    /**
     * 生成RuleNumber
     *
     * @param ruleAggrList 聚合根列表
     */
    void generateRuleNumber(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 处理双向Rule
     *
     * @param ruleAggrList 聚合根列表
     * @return 新的聚合根列表
     */
    List<ConfigurationRuleAggr> handleBothWayRule(List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 查找另一个双向Rule
     *
     * @param ruleAggr 聚合根
     * @return 双向Rule聚合根
     */
    ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr);

    /**
     * 校验Rule Driving下的Constrained打点不重复
     *
     * @param ruleAggrList 聚合根列表
     * @return 错误提示信息
     */
    String checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList);

}
