package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleGroupApplicationService {

    /**
     * 校验删除Group
     *
     * @param ruleAggrList 聚合根列表
     */
    void checkDeleteGroup(List<ConfigurationRuleAggr> ruleAggrList);

}
