package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleDomainService {

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

}
