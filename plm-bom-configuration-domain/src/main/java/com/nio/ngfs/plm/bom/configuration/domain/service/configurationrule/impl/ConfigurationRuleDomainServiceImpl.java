package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleDomainServiceImpl implements ConfigurationRuleDomainService {

    private final ConfigurationRuleRepository configurationRuleRepository;

    @Override
    public ConfigurationRuleAggr getAndCheckAggr(Long id) {
        ConfigurationRuleAggr aggr = configurationRuleRepository.find(id);
        if (aggr == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_NOT_EXIST);
        }
        return aggr;
    }

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

    @Override
    public ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(ruleAggr.getGroupId());
        if (CollectionUtils.isNotEmpty(ruleAggrList)) {
            ConfigurationRuleAggr anotherAggr = ruleAggrList.stream().filter(ruleAggr::isBothWayRule).findFirst().orElse(null);
            if (anotherAggr != null) {
                return anotherAggr;
            }
        }
        throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_NOT_FOUND);
    }

    @Override
    public String checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList) {
        List<RuleConstrainedOptionCompare> optionCompareList = ruleAggrList.stream().map(ruleAggr -> {
            Set<String> constrainedOptionCodeSet = ruleAggr.getOptionList().stream().filter(i -> !i.isMatrixValue(RuleOptionMatrixValueEnum.UNAVAILABLE))
                    .map(ConfigurationRuleOptionDo::getConstrainedOptionCode).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(constrainedOptionCodeSet)) {
                return null;
            }
            RuleConstrainedOptionCompare optionCompare = new RuleConstrainedOptionCompare();
            optionCompare.setDrivingOptionCode(ruleAggr.getOptionList().get(0).getConstrainedOptionCode());
            optionCompare.setConstrainedOptionCodeSet(constrainedOptionCodeSet);
            return optionCompare;
        }).filter(Objects::nonNull).toList();
        for (int i = 0; i < optionCompareList.size(); i++) {
            RuleConstrainedOptionCompare current = optionCompareList.get(i);
            if (current.isCompared()) {
                continue;
            }
            for (int j = i + 1; j < optionCompareList.size(); j++) {
                RuleConstrainedOptionCompare compare = optionCompareList.get(j);
                // Constrained Criteria打点信息重复
                if (!compare.isCompared() && Objects.equals(current.getConstrainedOptionCodeSet(), compare.getConstrainedOptionCodeSet())) {
                    current.getRepeatDrivingOptionCodeSet().add(current.getDrivingOptionCode());
                    current.getRepeatDrivingOptionCodeSet().add(compare.getDrivingOptionCode());
                    compare.setCompared(true);
                }
            }
        }
        String message = optionCompareList.stream().filter(i -> CollectionUtils.isNotEmpty(i.getRepeatDrivingOptionCodeSet()))
                .map(i -> String.join("/", i.getRepeatDrivingOptionCodeSet())).collect(Collectors.joining(","));
        if (StringUtils.isBlank(message)) {
            return message;
        }
        return "The Same Rule Existed In Driving Criteria Option " + message + ", Please Check!";
    }

    @Override
    public void releaseBothWayRule(List<ConfigurationRuleAggr> ruleAggrList) {
        List<ConfigurationRuleAggr> bothWayRuleAggrList = ruleAggrList.stream().filter(i -> i.getRulePurposeEnum().isBothWay()).toList();
        if (CollectionUtils.isEmpty(bothWayRuleAggrList)) {
            return;
        }
        // 已经匹配的双向Rule打标记
        for (ConfigurationRuleAggr aggr : bothWayRuleAggrList) {
            if (aggr.isBothWayPairMatch()) {
                continue;
            }
            for (ConfigurationRuleAggr anotherAggr : bothWayRuleAggrList) {
                if (aggr == anotherAggr || anotherAggr.isBothWayPairMatch()) {
                    continue;
                }
                if (aggr.isBothWayRule(anotherAggr)) {
                    aggr.setBothWayPairMatch(true);
                    anotherAggr.setBothWayPairMatch(true);
                }
            }
        }
        List<ConfigurationRuleAggr> lackBothWayRuleAggrList = bothWayRuleAggrList.stream().filter(i -> !i.isBothWayPairMatch()).toList();
        List<ConfigurationRuleAggr> groupRuleAggrList = configurationRuleRepository.queryByGroupIdList(LambdaUtil.map(lackBothWayRuleAggrList, ConfigurationRuleAggr::getGroupId));
        Map<Long, List<ConfigurationRuleAggr>> groupRuleAggrListGroup = LambdaUtil.groupBy(groupRuleAggrList, ConfigurationRuleAggr::getGroupId);
        lackBothWayRuleAggrList.forEach(aggr -> {
            List<ConfigurationRuleAggr> allGroupRuleAggrList = groupRuleAggrListGroup.getOrDefault(aggr.getGroupId(), Lists.newArrayList());
            ConfigurationRuleAggr anotherAggr = allGroupRuleAggrList.stream().filter(aggr::isBothWayRule).findFirst().orElse(null);
            if (anotherAggr != null) {
                if (anotherAggr.canRelease()) {
                    ruleAggrList.add(anotherAggr);
                }
            } else {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_NOT_FOUND);
            }
        });
    }

    @Data
    private static class RuleConstrainedOptionCompare {

        private String drivingOptionCode;

        private Set<String> constrainedOptionCodeSet;

        private boolean compared = false;

        private Set<String> repeatDrivingOptionCodeSet = Sets.newHashSet();

    }

}
