package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleGroupApplicationService {

    /**
     * 校验Driving Feature和Constrained Feature
     *
     * @param aggr         聚合根
     * @param ruleAggrList Rule聚合根列表
     */
    void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr aggr, List<ConfigurationRuleAggr> ruleAggrList);

    /**
     * 校验删除Group
     *
     * @param ruleAggrList 聚合根列表
     */
    void checkDeleteGroup(List<ConfigurationRuleAggr> ruleAggrList);

}
