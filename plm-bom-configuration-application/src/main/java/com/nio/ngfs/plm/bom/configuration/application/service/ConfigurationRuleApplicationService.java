package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleApplicationService {

    /**
     * 创建新的Rule
     *
     * @param ruleGroupAggr Rule Group聚合更
     * @param cmd           命令
     * @return ConfigurationRuleAggr列表
     */
    List<ConfigurationRuleAggr> createNewRule(ConfigurationRuleGroupAggr ruleGroupAggr, AddRuleCmd cmd);

}
