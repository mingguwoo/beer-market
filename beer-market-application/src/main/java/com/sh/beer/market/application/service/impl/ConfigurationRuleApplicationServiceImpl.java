package com.sh.beer.market.application.service.impl;


import com.sh.beer.market.application.service.ConfigurationRuleApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleApplicationServiceImpl implements ConfigurationRuleApplicationService {

    /*private final FeatureRepository featureRepository;
    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleDomainService configurationRuleDomainService;
    private final ProductContextFeatureRepository productContextFeatureRepository;

    @Override
    public EditConfigurationRuleContext buildEditConfigurationRuleContext(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList,
                                                                          List<ConfigurationRuleOptionDo> ruleOptionDoList) {
        Set<String> drivingOptionCodeSet = Sets.newHashSet();
        ruleAggrList.forEach(ruleAggr -> ruleAggr.getOptionList().forEach(option -> drivingOptionCodeSet.add(option.getDrivingOptionCode())));
        ruleOptionDoList.forEach(option -> drivingOptionCodeSet.add(option.getDrivingOptionCode()));
        Map<String, List<ConfigurationRuleAggr>> ruleAggrListMap = LambdaUtil.groupBy(ruleAggrList, i -> i.getOptionList().get(0).getDrivingOptionCode());
        Map<String, List<ConfigurationRuleOptionDo>> ruleOptionDoListGroup = LambdaUtil.groupBy(ruleOptionDoList, ConfigurationRuleOptionDo::getDrivingOptionCode);
        EditConfigurationRuleContext context = new EditConfigurationRuleContext();
        context.setRuleGroup(ruleGroupAggr);
        context.setGroupRuleList(ruleAggrList);
        context.setEditRuleList(LambdaUtil.map(drivingOptionCodeSet, drivingOptionCode -> {
            EditConfigurationRuleContext.EditConfigurationRule editConfigurationRule = new EditConfigurationRuleContext.EditConfigurationRule();
            editConfigurationRule.setDrivingOptionCode(drivingOptionCode);
            editConfigurationRule.setRuleOptionList(ruleOptionDoListGroup.getOrDefault(drivingOptionCode, Lists.newArrayList()));
            editConfigurationRule.setOptionEmptyOrAllUnavailable(CollectionUtils.isEmpty(editConfigurationRule.getRuleOptionList()) ||
                    editConfigurationRule.getRuleOptionList().stream().allMatch(ConfigurationRuleOptionDo::isMatrixValueUnavailable));
            // 按版本倒排
            List<ConfigurationRuleAggr> ruleList = ruleAggrListMap.getOrDefault(drivingOptionCode, Lists.newArrayList())
                    .stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion).reversed()).toList();
            // 排除存在不可见Rule的场景，不可见Rule不参与编辑
            if (ruleList.stream().anyMatch(ConfigurationRuleAggr::isRuleInvisible)) {
                return null;
            }
            editConfigurationRule.setReleasedRuleList(ruleList.stream().filter(ConfigurationRuleAggr::isStatusReleased).toList());
            List<ConfigurationRuleAggr> inWorkRuleList = ruleList.stream().filter(ConfigurationRuleAggr::isStatusInWork).toList();
            if (inWorkRuleList.size() > 1) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_IN_WORK_RULE_AT_MOST_ONE);
            }
            editConfigurationRule.setInWorkRule(inWorkRuleList.stream().findFirst().orElse(null));
            return editConfigurationRule;
        }));
        return context;
    }

    @Override
    public void preHandleEditRule(EditConfigurationRuleContext context) {
        context.getEditRuleList().forEach(editRule -> {
            // 预处理Rule新增
            preHandleRuleAddWithEdit(context, editRule);
            // 预处理Rule更新
            preHandleRuleUpdateWithEdit(context, editRule);
            // 预处理Rule删除
            preHandleRuleDeleteWithEdit(context, editRule);
        });
    }

    *//**
     * 预处理Rule新增（不存在In Work状态的Rule、打点不为空）
     *//*
    private void preHandleRuleAddWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.isNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // 新增Rule
        ConfigurationRuleAggr addRule = ConfigurationRuleFactory.createWithOptionList(context.getRuleGroup().getPurpose(), context.getRuleGroup().getUpdateUser(),
                editRule.getRuleOptionList());
        addRule.add();
        context.getAddRuleList().add(addRule);
    }

    *//**
     * 预处理Rule更新（存在In Work状态的Rule、打点不为空）
     *//*
    private void preHandleRuleUpdateWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.nonNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // 编辑打点
        ConfigurationRuleAggr updateRule = editRule.getInWorkRule();
        context.getUpdateRuleList().add(updateRule);
        if (updateRule.updateOption(editRule.getRuleOptionList())) {
            // 处理双向Rule编辑
            if (updateRule.isBothWayRule()) {
                ConfigurationRuleAggr anotherUpdateRule = configurationRuleDomainService.findAnotherBothWayRule(updateRule, context.getGroupRuleList());
                anotherUpdateRule.updateBothWayRuleOption(updateRule.getOptionList().stream().filter(ConfigurationRuleOptionDo::isNotDeleted)
                        .filter(ConfigurationRuleOptionDo::isNotMatrixValueUnavailable)
                        .map(ConfigurationRuleOptionDo::copyBothWayRuleOption).toList());
                context.getUpdateRuleList().add(anotherUpdateRule);
            }
        }
    }

    *//**
     * 预处理Rule删除（存在In Work状态的Rule、打点为空）
     *//*
    private void preHandleRuleDeleteWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.nonNull(editRule.getInWorkRule()) && editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        ConfigurationRuleAggr deleteRule = editRule.getInWorkRule();
        deleteRule.delete();
        context.getDeleteRuleList().add(deleteRule);
    }

    @Override
    public void checkAndProcessEditRule(EditConfigurationRuleContext context) {
        // 校验并处理删除的Rule
        checkAndProcessDeleteRule(context);
        List<ConfigurationRuleAggr> addOrUpdateRuleList = context.getAddOrUpdateRuleList();
        // 校验Driving Feature和Constrained Feature
        checkDrivingAndConstrainedFeature(context.getRuleGroup(), addOrUpdateRuleList);
        // 针对每一个Driving列，校验Constrained Feature下只能有一个Option为实心圆或-
        configurationRuleDomainService.checkOptionMatrixByConstrainedFeature(addOrUpdateRuleList);
        // 校验Rule Driving下的Constrained打点不重复
        List<ConfigurationRuleAggr> checkRepeatRuleList = Lists.newArrayList();
        checkRepeatRuleList.addAll(context.getGroupRuleList().stream().filter(ConfigurationRuleAggr::isStatusReleased)
                .filter(ConfigurationRuleAggr::isNotChangeTypeRemove).toList());
        checkRepeatRuleList.addAll(addOrUpdateRuleList);
        configurationRuleDomainService.checkRuleDrivingConstrainedRepeat(checkRepeatRuleList, context.getErrorMessageList());
        // 校验并处理新增的Rule
        checkAndProcessAddRule(context);
    }

    *//**
     * 校验并处理新增的Rule
     *//*
    private void checkAndProcessAddRule(EditConfigurationRuleContext context) {
        List<ConfigurationRuleAggr> addRuleList = context.getAddRuleList();
        if (CollectionUtils.isEmpty(addRuleList)) {
            return;
        }
        // 处理双向Rule
        addRuleList = configurationRuleDomainService.handleBothWayRule(addRuleList);
        // 分配Rule Number
        configurationRuleDomainService.generateRuleNumber(addRuleList);
        context.setAddRuleList(addRuleList);
    }

    *//**
     * 校验并处理删除的Rule
     *//*
    private void checkAndProcessDeleteRule(EditConfigurationRuleContext context) {
        List<ConfigurationRuleAggr> deleteRuleList = context.getDeleteRuleList();
        if (CollectionUtils.isEmpty(deleteRuleList)) {
            return;
        }
        if (context.getRuleGroup().getRulePurposeEnum().isBothWay()) {
            List<ConfigurationRuleAggr> deleteBothWayRuleList = Lists.newArrayList();
            // 处理双向Rule删除
            List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(context.getRuleGroup().getId());
            deleteRuleList.forEach(deleteRule -> {
                ConfigurationRuleAggr anotherDeleteRuleAggr = configurationRuleDomainService.findAnotherBothWayRule(deleteRule, ruleAggrList);
                anotherDeleteRuleAggr.delete();
                deleteBothWayRuleList.add(anotherDeleteRuleAggr);
            });
            context.getDeleteRuleList().addAll(deleteBothWayRuleList);
        }
    }

    @Override
    public void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList) {
        // Group选择的Driving Feature和Constrained Feature
        String groupDrivingFeature = ruleGroupAggr.getDrivingFeature();
        List<String> groupConstrainedFeatureList = ruleGroupAggr.getConstrainedFeatureList();
        // 矩阵打点的Driving Feature和Constrained Feature（只校验可见的Rule）
        List<ConfigurationRuleOptionDo> optionList = ruleAggrList.stream().filter(ConfigurationRuleAggr::isRuleVisible).flatMap(ruleAggr -> ruleAggr.getOptionList().stream())
                .filter(ConfigurationRuleOptionDo::isNotDeleted).toList();
        List<String> drivingFeatureCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getDrivingFeatureCode, true);
        List<String> constrainedFeatureCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getConstrainedFeatureCode, true);
        if (drivingFeatureCodeList.size() > 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_DRIVING_FEATURE_ONLY_SELECT_ONE);
        }
        String drivingFeatureCode = drivingFeatureCodeList.size() > 0 ? drivingFeatureCodeList.get(0) : null;
        // 校验Group选择的Driving Feature和矩阵打点的Driving Feature必须一致
        if (StringUtils.isBlank(groupDrivingFeature)) {
            if (StringUtils.isNotBlank(drivingFeatureCode)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_FEATURE_NOT_THE_SAME);
            }
        } else {
            if (StringUtils.isNotBlank(drivingFeatureCode) && !Objects.equals(groupDrivingFeature, drivingFeatureCode)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_FEATURE_NOT_THE_SAME);
            }
            // 校验Constrained Feature和Constrained Feature不能重复
            if (groupConstrainedFeatureList.contains(groupDrivingFeature)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_AND_CONSTRAINED_FEATURE_IS_REPEAT);
            }
        }
        // 校验Group选择的Constrained Feature和矩阵打点的Constrained Feature必须一致
        if (CollectionUtils.isEmpty(groupConstrainedFeatureList)) {
            if (CollectionUtils.isNotEmpty(constrainedFeatureCodeList)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_CONSTRAINED_FEATURE_NOT_THE_SAME);
            }
        } else {
            if (!new HashSet<>(groupConstrainedFeatureList).containsAll(constrainedFeatureCodeList)) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_CONSTRAINED_FEATURE_NOT_THE_SAME);
            }
        }
        Set<String> featureCodeSet = Sets.newHashSet();
        Optional.ofNullable(groupDrivingFeature).ifPresent(featureCodeSet::add);
        featureCodeSet.addAll(groupConstrainedFeatureList);
        Optional.ofNullable(drivingFeatureCode).ifPresent(featureCodeSet::add);
        featureCodeSet.addAll(constrainedFeatureCodeList);
        if (CollectionUtils.isEmpty(featureCodeSet)) {
            return;
        }
        // 校验Feature Code
        List<FeatureAggr> featureAggrList = featureRepository.queryByFeatureOptionCodeList(Lists.newArrayList(featureCodeSet));
        if (featureAggrList.size() != featureCodeSet.size()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        // 校验Product Context中的Feature行
        List<ProductContextFeatureAggr> productContextFeatureList = productContextFeatureRepository.queryByModelCodeAndFeatureCode(ruleGroupAggr.getModel(),
                Lists.newArrayList(featureCodeSet));
        if (productContextFeatureList.size() != featureCodeSet.size() || !productContextFeatureList.stream().allMatch(ProductContextFeatureAggr::isTypeFeature)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONTEXT_FEATURE_NOT_EXIST);
        }
        checkDrivingAndConstrainedFeature2(ruleGroupAggr.getRulePurposeEnum(), groupDrivingFeature, groupConstrainedFeatureList, featureAggrList);
        checkDrivingAndConstrainedFeature2(ruleGroupAggr.getRulePurposeEnum(), drivingFeatureCode, constrainedFeatureCodeList, featureAggrList);
    }

    private void checkDrivingAndConstrainedFeature2(ConfigurationRulePurposeEnum purposeEnum, String drivingFeatureCode, List<String> constrainedFeatureCodeCodeList, List<FeatureAggr> featureAggrList) {
        Map<String, FeatureAggr> featureAggrMap = LambdaUtil.toKeyMap(featureAggrList, FeatureAggr::getFeatureCode);
        // 校验Driving Feature
        if (StringUtils.isNotBlank(drivingFeatureCode)) {
            checkFeatureCatalog(featureAggrMap.get(drivingFeatureCode), FeatureCatalogEnum.SALES);
        }
        // 校验Constrained Feature
        if (CollectionUtils.isNotEmpty(constrainedFeatureCodeCodeList)) {
            if (purposeEnum != ConfigurationRulePurposeEnum.SALES_TO_ENG && purposeEnum != ConfigurationRulePurposeEnum.SALES_TO_SALES) {
                if (constrainedFeatureCodeCodeList.size() > 1) {
                    throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_CONSTRAINED_FEATURE_ONLY_SELECT_ONE);
                }
            }
            constrainedFeatureCodeCodeList.forEach(constrainedFeature -> {
                if (purposeEnum == ConfigurationRulePurposeEnum.SALES_TO_ENG) {
                    checkFeatureCatalog(featureAggrMap.get(constrainedFeature), FeatureCatalogEnum.ENGINEERING);
                } else {
                    checkFeatureCatalog(featureAggrMap.get(constrainedFeature), FeatureCatalogEnum.SALES);
                }
            });
        }
    }

    *//**
     * 校验Feature的Catalog
     *//*
    private void checkFeatureCatalog(FeatureAggr featureAggr, FeatureCatalogEnum catalogEnum) {
        if (featureAggr == null || !featureAggr.isFeature()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        if (!featureAggr.isCatalog(catalogEnum)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_FEATURE_CATALOG_NOT_MATCH);
        }
    }*/

}
