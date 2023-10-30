package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.bom.share.utils.VersionUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.RuleOptionDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    private final List<Integer> purposeLists = Lists.newArrayList(
            ConfigurationRulePurposeEnum.SALES_TO_ENG.getCode(),
            ConfigurationRulePurposeEnum.SALES_TO_SALES.getCode(),
            ConfigurationRulePurposeEnum.SALES_INCLUSIVE_SALES.getCode(),
            ConfigurationRulePurposeEnum.SALES_EXCLUSIVE_SALES.getCode());

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
    public List<ConfigurationRuleAggr> batchFindAnotherBothWayRule(List<ConfigurationRuleAggr> ruleAggrList) {
        // 过滤双向Rule
        List<ConfigurationRuleAggr> bothWayRuleAggrList = ruleAggrList.stream().filter(ConfigurationRuleAggr::isBothWayRule).toList();
        if (CollectionUtils.isEmpty(bothWayRuleAggrList)) {
            return Lists.newArrayList();
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
        List<ConfigurationRuleAggr> anotherBothWayRuleAggrList = Lists.newArrayList();
        List<ConfigurationRuleAggr> lackBothWayRuleAggrList = bothWayRuleAggrList.stream().filter(i -> !i.isBothWayPairMatch()).toList();
        List<ConfigurationRuleAggr> groupRuleAggrList = configurationRuleRepository.queryByGroupIdList(LambdaUtil.map(lackBothWayRuleAggrList, ConfigurationRuleAggr::getGroupId));
        Map<Long, List<ConfigurationRuleAggr>> groupRuleAggrListGroup = LambdaUtil.groupBy(groupRuleAggrList, ConfigurationRuleAggr::getGroupId);
        lackBothWayRuleAggrList.forEach(aggr -> {
            List<ConfigurationRuleAggr> allGroupRuleAggrList = groupRuleAggrListGroup.getOrDefault(aggr.getGroupId(), Lists.newArrayList());
            ConfigurationRuleAggr anotherAggr = findAnotherBothWayRule(aggr, allGroupRuleAggrList);
            // 添加另一个双向Rule
            anotherBothWayRuleAggrList.add(anotherAggr);
        });
        return anotherBothWayRuleAggrList;
    }

    @Override
    public String checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList) {
        List<RuleConstrainedOptionCompare> optionCompareList = ruleAggrList.stream().filter(ConfigurationRuleAggr::isVisible).map(ruleAggr -> {
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
    public void releaseBothWayRule(List<ConfigurationRuleAggr> ruleAggrList) {
        List<ConfigurationRuleAggr> anotherBothWayRuleAggrList = batchFindAnotherBothWayRule(ruleAggrList);
        anotherBothWayRuleAggrList.forEach(anotherBothWayRule -> {
            if (anotherBothWayRule.canRelease()) {
                ruleAggrList.add(anotherBothWayRule);
            } else {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_RULE_ALREADY_RELEASED);
            }
        });
    }


    /**
     * Eff-in值校验
     */
    @Override
    public void checkNextRevConfigurationRule(List<Long> ruleIds, Date effIn, Date effOut) {

        List<ConfigurationRuleAggr> ruleAggrList =
                configurationRuleRepository.queryByRuleIdList(ruleIds, false);
        if (CollectionUtils.isEmpty(ruleAggrList)) {
            throw new BusinessException(ConfigErrorCode.RULE_ID_ERROR);
        }
        ruleAggrList.forEach(ruleAggr -> {
            //所选日期是否早于/等于Rule条目的Eff-out值
            if (Objects.nonNull(effIn) && effIn.compareTo(ruleAggr.getEffOut()) > 0) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.EFF_OUT_TIME_ERROR.getMessage(), ruleAggr.getRuleNumber(), ruleAggr.getRuleVersion()));
            }
            if (Objects.nonNull(effOut) && ruleAggr.getEffIn().compareTo(effOut) > 0) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.EFF_IN_TIME_ERROR.getMessage(), ruleAggr.getRuleNumber(), ruleAggr.getRuleVersion()));
            }
        });
        if (Objects.isNull(effOut)) {
            return;
        }
        List<String> ruleNumbers = ruleAggrList.stream().filter(x -> StringUtils.equalsAny(x.getChangeType()
                , ConfigurationRuleChangeTypeEnum.ADD.getChangeType(), ConfigurationRuleChangeTypeEnum.MODIFY.getChangeType())).map(ConfigurationRuleAggr::getRuleNumber).distinct().toList();
        if (CollectionUtils.isEmpty(ruleNumbers)) {
            return;
        }
        List<ConfigurationRuleAggr> ruleAggrs = configurationRuleRepository.queryByRuleNumbers(ruleNumbers);
        ruleAggrList.forEach(x -> {
            String nextRev = VersionUtils.findNextRev(x.getRuleNumber());
            //当勾选Rule条目的Change Type为Add或Modify时，系统判断是否存在相邻的下一个Rev条目
            if (ruleAggrs.stream().noneMatch(y -> StringUtils.equals(y.getRuleNumber(), x.getRuleNumber()) &&
                    StringUtils.equals(y.getRuleVersion(), nextRev))) {
                throw new BusinessException(ConfigErrorCode.RULE_ID_ERROR.getCode(),
                        MessageFormat.format(ConfigErrorCode.RULE_ID_ERROR.getMessage(), x.getRuleNumber(), x.getRuleVersion()));
            }
        });
    }

    @Override
    public void updateEffInOrEffOut(List<Long> ruleIds, ConfigurationRuleAggr updateInfo) {
        Date date = new Date();
        Date effIn = updateInfo.getEffIn();
        Date effOut = updateInfo.getEffOut();
        String userName = updateInfo.getUpdateUser();

        configurationRuleRepository.batchUpdate(ruleIds.stream().map(x -> {
            ConfigurationRuleAggr ruleAggr = new ConfigurationRuleAggr();
            ruleAggr.setId(x);
            if (Objects.nonNull(effIn)) {
                ruleAggr.setEffIn(effIn);
            }
            if (Objects.nonNull(effOut)) {
                ruleAggr.setEffOut(effOut);
            }
            ruleAggr.setUpdateTime(date);
            ruleAggr.setUpdateUser(userName);
            return ruleAggr;
        }).toList());
    }

    @Override
    public void checkConfigurationRuleRemove(List<ConfigurationRuleAggr> ruleAggrList) {
        //校验数据是否不为remove
        ruleAggrList.forEach(ruleInfo -> {
            if (StringUtils.equals(ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType(), ruleInfo.getChangeType())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.RULE_CHANGE_TYPE_ERROR.getMessage(), ruleInfo.getRuleNumber(), ruleInfo.getRuleVersion()));
            }
            if (!purposeLists.contains(ruleInfo.getPurpose())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.PURPOSE_ERROR.getMessage(), ruleInfo.getRuleNumber(), ruleInfo.getRuleVersion()));
            }
        });
        //根据rule_number批量查询
        List<ConfigurationRuleAggr> configurationRules =
                configurationRuleRepository.queryByRuleNumbers(ruleAggrList.stream().map(ConfigurationRuleAggr::getRuleNumber).distinct().toList());
        //Rule最新Rev条目 且 Status为Released（Change Type不为Remove）
        Map<String, List<ConfigurationRuleAggr>> configurationRuleByRuleNumberMap = configurationRules.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));
        configurationRuleByRuleNumberMap.forEach((ruleNumber, configurationRuleAggrs) -> {
            //Rule最新Rev条目 且 Status为Released（Change Type不为Remove）
            ConfigurationRuleAggr configurationRuleAggr =
                    configurationRuleAggrs.stream().max(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion)).orElse(new ConfigurationRuleAggr());
            if (StringUtils.equals(configurationRuleAggr.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()) &&
                    !StringUtils.equals(configurationRuleAggr.getChangeType(), ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.RULE_CHANGE_TYPE_ERROR.getMessage(), ruleNumber, configurationRuleAggr.getRuleVersion()));
            }
        });
    }

    @Override
    public void checkHardRule(List<ConfigurationRuleAggr> configurationRuleAggrs) {

        //基于相同的Rule ID，高版本的Eff-in必须等于相邻低版本的Eff-out
        Map<String, List<ConfigurationRuleAggr>> ruleNumberMaps =
                configurationRuleAggrs.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));

        ruleNumberMaps.forEach((ruleNumber, ruleAggrs) -> {

            List<ConfigurationRuleAggr> sortRuleAggrs =
                    ruleAggrs.stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion)).toList();

            for (int i = 0; i < sortRuleAggrs.size() - 1; i++) {

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
