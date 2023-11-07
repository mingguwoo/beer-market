package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
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
    public void checkRuleDrivingConstrainedRepeat(List<ConfigurationRuleAggr> ruleAggrList) {
        List<RuleConstrainedOptionCompare> optionCompareList = ruleAggrList.stream().filter(ConfigurationRuleAggr::isVisible).map(ruleAggr -> {
            Set<String> constrainedOptionCodeSet = ruleAggr.getOptionList().stream()
                    .filter(ConfigurationRuleOptionDo::isNotDeleted)
                    .filter(ConfigurationRuleOptionDo::isNotMatrixValueUnavailable)
                    .map(ConfigurationRuleOptionDo::getConstrainedOptionCode).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(constrainedOptionCodeSet)) {
                return null;
            }
            RuleConstrainedOptionCompare optionCompare = new RuleConstrainedOptionCompare();
            optionCompare.setDrivingOptionCode(ruleAggr.getOptionList().get(0).getDrivingOptionCode());
            optionCompare.setConstrainedOptionCodeSet(constrainedOptionCodeSet);
            return optionCompare;
        }).filter(Objects::nonNull).sorted(Comparator.comparing(RuleConstrainedOptionCompare::getDrivingOptionCode)).toList();
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
            return;
        }
        throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_THE_SAME_RULE_EXISTED.getCode(),
                String.format(ConfigErrorCode.CONFIGURATION_RULE_THE_SAME_RULE_EXISTED.getMessage(), message));
    }

    @Override
    public void checkOptionMatrixByConstrainedFeature(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList.stream().filter(ConfigurationRuleAggr::isVisible).forEach(ruleAggr -> {
            ruleAggr.getOptionList().stream().filter(ConfigurationRuleOptionDo::isNotMatrixValueUnavailable)
                    .filter(ConfigurationRuleOptionDo::isNotDeleted)
                    .collect(Collectors.groupingBy(ConfigurationRuleOptionDo::getConstrainedFeatureCode))
                    .forEach((constrainedFeatureCode, optionList) -> {
                        if (optionList.size() > 1) {
                            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_ONLY_ONE_OPTION_PER_CONSTRAINED_BY_DRIVING.getCode(),
                                    String.format("It Can Only Has One Option Be Inclusive/Exclusive In Constrained Feature %s By Driving Feature %s",
                                            constrainedFeatureCode, optionList.get(0).getDrivingFeatureCode()));
                        }
                    });
        });
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
            //所选日期是否晚于/等于Rule条目的Eff-in值
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
            String nextRev = VersionUtils.findNextRev(x.getRuleVersion());
            //当勾选Rule条目的Change Type为Add或Modify时，系统判断是否存在相邻的下一个Rev条目
            if (ruleAggrs.stream().noneMatch(y -> StringUtils.equals(y.getRuleNumber(), x.getRuleNumber()) &&
                    StringUtils.equals(y.getRuleVersion(), nextRev))) {
                throw new BusinessException(ConfigErrorCode.EFF_OUT_ERROR.getCode(),
                        MessageFormat.format(ConfigErrorCode.EFF_OUT_ERROR.getMessage(), x.getRuleNumber(), x.getRuleVersion()));
            }
        });
    }

    @Override
    public void updateEffInOrEffOut(List<Long> ruleIds, ConfigurationRuleAggr updateInfo) {
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
    public List<Long> checkHardRule(List<ConfigurationRuleAggr> configurationRuleAggrs) {
        //基于相同的Rule ID，高版本的Eff-in必须等于相邻低版本的Eff-out
        Map<String, List<ConfigurationRuleAggr>> ruleNumberMaps =
                configurationRuleAggrs.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));
        List<Long> redRuleIds = Lists.newArrayList();
        ruleNumberMaps.forEach((ruleNumber, ruleAggrs) -> {
            if (ruleAggrs.size() > 1) {
                List<ConfigurationRuleAggr> sortRuleAggrs =
                        ruleAggrs.stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion)).toList();
                for (int i = 0; i < sortRuleAggrs.size() - 1; i++) {
                    //高版本的Eff-in必须等于相邻低版本的Eff-out
                    if (sortRuleAggrs.get(i).getEffOut().compareTo(sortRuleAggrs.get(i + 1).getEffIn()) != 0) {
                        redRuleIds.add(sortRuleAggrs.get(i).getId());
                        redRuleIds.add(sortRuleAggrs.get(i + 1).getId());
                    }
                }
            }
        });
        /**
         * 2.针对Sales <—> Sales、Sales X Sales的Purpose
         * 由于系统会生成成对的Rule，因此基于相同版本的成对Rule，需满足如下校验：
         * 两条Rule的Eff-in值必须一致，两条Rule的Eff-out值必须一致
         */
        configurationRuleAggrs.stream().filter(x ->
                Objects.equals(x.getPurpose(), ConfigurationRulePurposeEnum.SALES_INCLUSIVE_SALES.getCode()) || Objects.equals(x.getPurpose(), ConfigurationRulePurposeEnum.SALES_EXCLUSIVE_SALES.getCode())).toList().forEach(
                configurationRuleAggr -> {
                    ConfigurationRuleAggr compareConfigurationRuleAggr = findAnotherBothWayRule(configurationRuleAggr, configurationRuleAggrs.stream().filter(y -> Objects.equals(y.getGroupId(), configurationRuleAggr.getGroupId())).toList());
                    if (Objects.nonNull(compareConfigurationRuleAggr) && (configurationRuleAggr.getEffIn().compareTo(compareConfigurationRuleAggr.getEffIn()) != 0 || configurationRuleAggr.getEffOut().compareTo(compareConfigurationRuleAggr.getEffOut()) != 0)) {
                        redRuleIds.add(compareConfigurationRuleAggr.getId());
                        redRuleIds.add(configurationRuleAggr.getId());
                    }
                });

        return redRuleIds.stream().distinct().toList();

    }

    /**
     * 软校验
     *
     * @param configurationRuleAggrs
     * @return
     */
    @Override
    public List<Long> checkSoftRule(List<ConfigurationRuleAggr> configurationRuleAggrs) {
        List<Long> sortList = Lists.newArrayList();
        //当Change Type为Remove时，Eff-out时间不能为9999/12/31（即未设置Eff-out时间，则提示）
        List<Long> ids = configurationRuleAggrs.stream().filter(x -> StringUtils.equals(x.getChangeType()
                , ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType()) && (Objects.isNull(x.getEffOut()) ||
                StringUtils.equals("9999-12-31", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, x.getEffOut())))).map(ConfigurationRuleAggr::getId).toList();
        if (CollectionUtils.isNotEmpty(ids)) {
            sortList.addAll(ids);
        }

        Map<String, List<ConfigurationRuleAggr>> configurationRuleMaps =
                configurationRuleAggrs.stream().filter(x -> purposeLists.contains(x.getPurpose()) && CollectionUtils.isNotEmpty(x.getOptionList())).collect(Collectors.groupingBy(x -> x.getPurpose() + "#" + x.getRuleType()));

        configurationRuleMaps.forEach((configurationRule, ruleAggrs) -> {
            ruleAggrs.forEach(ruleAggr -> {
                ruleAggr.setDrivingOptionCode(ruleAggr.getOptionList().stream().map(ConfigurationRuleOptionDo::getDrivingOptionCode).distinct().toList().get(0));
            });
            sortAndCompare(ruleAggrs.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getDrivingOptionCode)), sortList);
        });

        Map<String, List<ConfigurationRuleAggr>> baseVehicleRuleMaps =
                configurationRuleAggrs.stream().filter(x ->
                                Objects.equals(ConfigurationRulePurposeEnum.BASE_VEHICLE_TO_SALES.getCode(), x.getPurpose())
                                        && CollectionUtils.isNotEmpty(x.getOptionList()))
                        .collect(Collectors.groupingBy(x -> x.getPurpose() + "#" + x.getRuleType()));

        baseVehicleRuleMaps.forEach((configurationRule, ruleAggrs) -> {
            ruleAggrs.forEach(ruleAggr -> {
                ruleAggr.setDrivingOptionCode(ruleAggr.getOptionList().stream().map(ConfigurationRuleOptionDo::getDrivingOptionCode).distinct().toList().get(0));
                ruleAggr.setConstrainedOptionCode(ruleAggr.getOptionList().stream().map(ConfigurationRuleOptionDo::getConstrainedOptionCode).distinct().toList().get(0));
            });
            sortAndCompare(ruleAggrs.stream().collect(Collectors.groupingBy(x -> x.getDrivingOptionCode() + "#" + x.getConstrainedOptionCode())), sortList);
        });

        return sortList.stream().distinct().toList();
    }

    /**
     * 排序 进行时间比对
     *
     * @param optionCodes
     * @param sortList
     */
    public void sortAndCompare(Map<String, List<ConfigurationRuleAggr>> optionCodes, List<Long> sortList) {
        optionCodes.forEach((drivingCode, rules) -> {
            List<ConfigurationRuleAggr> configurationRules = Lists.newArrayList();
            Map<String, List<ConfigurationRuleAggr>>
                    ruleAggrMaps = rules.stream().filter(x -> StringUtils.equalsAny(x.getChangeType()
                            , ConfigurationRuleChangeTypeEnum.ADD.getChangeType(), ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType())).
                    sorted(Comparator.comparing(ConfigurationRuleAggr::getCreateTime)).
                    collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber, LinkedHashMap::new, Collectors.toList()));

            ruleAggrMaps.forEach((k, v) -> {
                ConfigurationRuleAggr addRule = queryConfigurationRuleAggr(ConfigurationRuleChangeTypeEnum.ADD.getChangeType(), v);
                if (Objects.isNull(addRule)) {
                    return;
                }
                ConfigurationRuleAggr removeRule = queryConfigurationRuleAggr(ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType(), v);
                configurationRules.add(addRule);
                if (Objects.nonNull(removeRule)) {
                    configurationRules.add(removeRule);
                }
            });

            if (CollectionUtils.isEmpty(configurationRules) || configurationRules.size() <= 2) {
                return;
            }

            List<ConfigurationRuleAggr> finalConfigurationRules =
                    configurationRules.stream().distinct().sorted(Comparator.comparing(ConfigurationRuleAggr::getCreateTime)).toList();

            ConfigurationRuleAggr configurationRuleAggr = null;
            ConfigurationRuleAggr nextConfigurationRuleAggr = null;
            for (int i = 1; i < finalConfigurationRules.size() - 2; i++) {
                if (Objects.isNull(configurationRuleAggr)) {
                    configurationRuleAggr = finalConfigurationRules.get(i);
                }
                if (Objects.isNull(nextConfigurationRuleAggr)) {
                    nextConfigurationRuleAggr = finalConfigurationRules.get(i + 1);
                }

                if (nextConfigurationRuleAggr.getEffIn().compareTo(configurationRuleAggr.getEffOut()) >= 0) {
                    sortList.add(nextConfigurationRuleAggr.getId());
                    sortList.add(configurationRuleAggr.getId());
                }
                configurationRuleAggr = nextConfigurationRuleAggr;
                nextConfigurationRuleAggr = finalConfigurationRules.get(i + 2);
            }
        });
    }


    public ConfigurationRuleAggr queryConfigurationRuleAggr(String changeType, List<ConfigurationRuleAggr> configurationRuleAggrs) {
        return configurationRuleAggrs.stream().filter(x -> StringUtils.equals(x.getChangeType(), changeType))
                .max(Comparator.comparing(ConfigurationRuleAggr::getId)).orElse(null);
    }


    @Data
    private static class RuleConstrainedOptionCompare {

        private String drivingOptionCode;

        private Set<String> constrainedOptionCodeSet;

        private boolean compared = false;

        private Set<String> repeatDrivingOptionCodeSet = Sets.newHashSet();

    }

}
