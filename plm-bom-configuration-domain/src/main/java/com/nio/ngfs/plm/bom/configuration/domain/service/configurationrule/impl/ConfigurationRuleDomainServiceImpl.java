package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleDomainServiceImpl implements ConfigurationRuleDomainService {

    private final ConfigurationRuleRepository configurationRuleRepository;

    @Override
    public void generateRuleNumber(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList = ruleAggrList.stream().filter(i -> StringUtils.isBlank(i.getRuleNumber())).toList();
        ArrayDeque<String> ruleNumberQueue = new ArrayDeque<>(configurationRuleRepository.applyRuleNumber(ruleAggrList.size()));
        ruleAggrList.forEach(ruleAggr -> ruleAggr.setRuleNumber(ruleNumberQueue.pop()));
    }

    @Override
    public List<ConfigurationRuleAggr> handleBothWayRule(List<ConfigurationRuleAggr> ruleAggrList) {
        if (CollectionUtils.isEmpty(ruleAggrList) || !ruleAggrList.get(0).getRulePurposeEnum().isBothWay()) {
            return Lists.newArrayList();
        }
        List<ConfigurationRuleAggr> newRuleAggrList = Lists.newArrayList(ruleAggrList);
        ruleAggrList.forEach(ruleAggr -> newRuleAggrList.add(ruleAggr.copyBothWayRule()));
        return newRuleAggrList;
    }

}
