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
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.context.EditConfigurationRuleContext;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.RuleOptionDto;
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
        List<RuleOptionDto> ruleOptionList = LambdaUtil.map(cmd.getRuleOptionList(), i -> !Objects.equals(i.getMatrixValue(),
                RuleOptionMatrixValueEnum.UNAVAILABLE.getCode()), Function.identity());
        if (CollectionUtils.isEmpty(ruleOptionList)) {
            return Lists.newArrayList();
        }
        // 按drivingOptionCode分组，生成Rule聚合根
        return LambdaUtil.groupBy(ruleOptionList, RuleOptionDto::getDrivingOptionCode)
                .values().stream().map(ruleOptionDtoList -> ConfigurationRuleFactory.create(cmd.getPurpose(), cmd.getCreateUser(), ruleOptionDtoList)).toList();
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
            return ruleAggrList;
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
    public ConfigurationRuleAggr findAnotherBothWayRule(ConfigurationRuleAggr ruleAggr, List<ConfigurationRuleAggr> groupRuleAggrList) {
        List<ConfigurationRuleAggr> anotherRuleAggrList = Optional.ofNullable(groupRuleAggrList).orElse(Lists.newArrayList()).stream().filter(ruleAggr::isBothWayRule).toList();
        if (CollectionUtils.isEmpty(anotherRuleAggrList)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_NOT_FOUND);
        } else if (anotherRuleAggrList.size() > CommonConstants.INT_ONE) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_IS_MORE_THAN_TWO);
        }
        return anotherRuleAggrList.get(0);
    }

    @Override
    public String checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList) {
        List<RuleConstrainedOptionCompare> optionCompareList = ruleAggrList.stream().map(ruleAggr -> {
            Set<String> constrainedOptionCodeSet = ruleAggr.getOptionList().stream()
                    .filter(ConfigurationRuleOptionDo::isNotDeleted)
                    .filter(i -> !i.isMatrixValue(RuleOptionMatrixValueEnum.UNAVAILABLE))
                    .map(ConfigurationRuleOptionDo::getConstrainedOptionCode).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(constrainedOptionCodeSet)) {
                return null;
            }
            RuleConstrainedOptionCompare optionCompare = new RuleConstrainedOptionCompare();
            optionCompare.setDrivingOptionCode(ruleAggr.getOptionList().get(0).getDrivingOptionCode());
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
                .map(i -> String.join("/", i.getRepeatDrivingOptionCodeSet().stream().sorted(String::compareTo).toList())).collect(Collectors.joining(","));
        if (StringUtils.isBlank(message)) {
            return message;
        }
        return "The Same Rule Existed In Driving Criteria Option " + message + ", Please Check!";
    }

    @Override
    public void editRule(EditConfigurationRuleContext context) {
        context.getEditRuleList().forEach(editRule -> {
            // 处理Rule新增
            handleRuleAddWithEdit(context, editRule);
            // 处理Rule更新
            handleRuleUpdateWithEdit(context, editRule);
            // 处理Rule删除
            handleRuleDeleteWithEdit(context, editRule);
        });
    }

    @Override
    public List<ConfigurationRuleAggr> deleteRule(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList.forEach(ConfigurationRuleAggr::delete);
        return null;
    }

    /**
     * 处理Rule新增（不存在In Work状态的Rule、打点不为空）
     */
    private void handleRuleAddWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.isNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // Driving Criteria Option下有已发布的Rule版本，不可新增Rule
        if (CollectionUtils.isNotEmpty(editRule.getReleasedRuleList())) {
            context.addErrorMessage(String.format("The Rule Of Driving Criteria Option %s (Rev:%s) Is Already Released, Can Not Create The Same Rule In Driving Criteria Option" +
                    " %s, Please Check!", editRule.getDrivingOptionCode(), editRule.getLatestReleasedRule().getRuleVersion(), editRule.getDrivingOptionCode()));
            return;
        }
        // 新增Rule
        ConfigurationRuleAggr ruleAggr = ConfigurationRuleFactory.createWithOptionList(context.getPurposeEnum().getCode(), context.getUpdateUser(), editRule.getRuleOptionList());
        context.getAddRuleList().add(ruleAggr);
    }

    /**
     * 处理Rule更新（存在In Work状态的Rule、打点不为空）
     */
    private void handleRuleUpdateWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.nonNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // 编辑打点
        editRule.getInWorkRule().editOption(editRule.getRuleOptionList());
        context.getUpdateRuleList().add(editRule.getInWorkRule());

    }

    /**
     * 处理Rule删除（存在In Work状态的Rule、打点为空）
     */
    private void handleRuleDeleteWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.nonNull(editRule.getInWorkRule()) && editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        context.getDeleteRuleList().add(editRule.getInWorkRule());
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

    @Data
    private static class RuleConstrainedOptionCompare {

        private String drivingOptionCode;

        private Set<String> constrainedOptionCodeSet;

        private boolean compared = false;

        private Set<String> repeatDrivingOptionCodeSet = Sets.newHashSet();

    }

}
