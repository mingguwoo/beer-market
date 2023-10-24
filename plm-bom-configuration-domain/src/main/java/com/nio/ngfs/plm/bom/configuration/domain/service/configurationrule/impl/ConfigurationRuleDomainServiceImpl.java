package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    public List<ConfigurationRuleAggr> createNewRule(AddRuleCmd cmd) {
        // 筛选有效的打点
        List<AddRuleCmd.RuleOptionDto> ruleOptionList = LambdaUtil.map(cmd.getRuleOptionList(), i -> !Objects.equals(i.getMatrixValue(),
                RuleOptionMatrixValueEnum.UNAVAILABLE.getCode()), Function.identity());
        if (CollectionUtils.isEmpty(ruleOptionList)) {
            return Lists.newArrayList();
        }
        // 按drivingOptionCode分组，生成Rule聚合根
        return LambdaUtil.groupBy(ruleOptionList, AddRuleCmd.RuleOptionDto::getDrivingOptionCode)
                .values().stream().map(ruleOptionDtoList -> ConfigurationRuleFactory.create(cmd, ruleOptionDtoList)).toList();
    }

    @Override
    public void generateRuleNumber(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList = ruleAggrList.stream().filter(i -> StringUtils.isBlank(i.getRuleNumber())).toList();
        ArrayDeque<String> ruleNumberQueue = new ArrayDeque<>(configurationRuleRepository.applyRuleNumber(ruleAggrList.size()));
        ruleAggrList.forEach(ruleAggr -> ruleAggr.setRuleNumber(ruleNumberQueue.pop()));
    }

    @Override
    public List<ConfigurationRuleAggr> handleBothWayRule(List<ConfigurationRuleAggr> ruleAggrList) {
        if (CollectionUtils.isEmpty(ruleAggrList) || !ruleAggrList.get(0).isBothWayRule()) {
            return Lists.newArrayList();
        }
        List<ConfigurationRuleAggr> newRuleAggrList = Lists.newArrayList();
        ruleAggrList.forEach(ruleAggr -> {
            newRuleAggrList.add(ruleAggr);
            newRuleAggrList.add(ruleAggr.copyBothWayRule());
        });
        return newRuleAggrList;
    }

    @Override
    public ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(ruleAggr.getGroupId());
        return findAnotherBothWayRule(ruleAggr, ruleAggrList);
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
        // 过滤双向Rule
        List<ConfigurationRuleAggr> bothWayRuleAggrList = ruleAggrList.stream().filter(ConfigurationRuleAggr::isBothWayRule).toList();
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
            ConfigurationRuleAggr anotherAggr = findAnotherBothWayRule(aggr, allGroupRuleAggrList);
            if (anotherAggr.canRelease()) {
                // 添加另一个双向Rule
                ruleAggrList.add(anotherAggr);
            } else {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_ALREADY_RELEASED);
            }
        });
    }

    /**
     * 查找另一个双向Rule
     *
     * @param ruleAggr          其中一个双向Rule
     * @param groupRuleAggrList Group下的Rule列表
     * @return 另一个双向Rule
     */
    private ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr, List<ConfigurationRuleAggr> groupRuleAggrList) {
        List<ConfigurationRuleAggr> anotherRuleAggrList = Optional.ofNullable(groupRuleAggrList).orElse(Lists.newArrayList()).stream().filter(ruleAggr::isBothWayRule).toList();
        if (CollectionUtils.isEmpty(anotherRuleAggrList)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_NOT_FOUND);
        } else if (anotherRuleAggrList.size() > CommonConstants.INT_ONE) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_IS_MORE_THAN_TWO);
        }
        return anotherRuleAggrList.get(0);
    }

    @Override
    public String getReviseVersion(String version) {
        char lastChar = 'Z';
        StringBuilder newVersion = new StringBuilder(version);
        for (int i = version.length()-1; i >= 0; i--){
            if (version.charAt(i) < lastChar){
                char newChar = (char)(version.charAt(i)+1);
                newVersion.replace(i,i+1,String.valueOf(newChar));
                return newVersion.toString();
            }
            else{
                newVersion.replace(i,i+1,"A");
            }
        }
        throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_VERSION_OVERFLOW);
    }

    @Data
    private static class RuleConstrainedOptionCompare {

        private String drivingOptionCode;

        private Set<String> constrainedOptionCodeSet;

        private boolean compared = false;

        private Set<String> repeatDrivingOptionCodeSet = Sets.newHashSet();

    }

}
