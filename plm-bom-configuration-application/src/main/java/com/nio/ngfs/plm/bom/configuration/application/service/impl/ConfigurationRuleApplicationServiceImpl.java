package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.context.EditConfigurationRuleContext;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleApplicationServiceImpl implements ConfigurationRuleApplicationService {

    private final FeatureRepository featureRepository;
    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleDomainService configurationRuleDomainService;

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
        context.setEditRuleList(LambdaUtil.map(drivingOptionCodeSet, drivingOptionCode -> {
            EditConfigurationRuleContext.EditConfigurationRule editConfigurationRule = new EditConfigurationRuleContext.EditConfigurationRule();
            editConfigurationRule.setDrivingOptionCode(drivingOptionCode);
            editConfigurationRule.setRuleOptionList(ruleOptionDoListGroup.getOrDefault(drivingOptionCode, Lists.newArrayList()));
            editConfigurationRule.setOptionEmptyOrAllUnavailable(CollectionUtils.isEmpty(editConfigurationRule.getRuleOptionList()) ||
                    editConfigurationRule.getRuleOptionList().stream().allMatch(ConfigurationRuleOptionDo::isMatrixValueUnavailable));
            // 按版本倒排
            List<ConfigurationRuleAggr> ruleList = ruleAggrListMap.getOrDefault(drivingOptionCode, Lists.newArrayList())
                    .stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion).reversed()).toList();
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

    /**
     * 预处理Rule新增（不存在In Work状态的Rule、打点不为空）
     */
    private void preHandleRuleAddWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.isNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // Driving Criteria Option下有已发布的Rule版本，不可新增Rule
        editRule.getReleasedRuleList().stream().filter(i -> !i.isChangeTypeRemove()).findFirst().ifPresent(releaseRule ->
                context.addErrorMessage(String.format("The Rule Of Driving Criteria Option %s (Rev:%s) Is Already Released, Can Not Create The Same Rule In Driving Criteria Option" +
                        " %s, Please Check!", editRule.getDrivingOptionCode(), releaseRule.getRuleVersion(), editRule.getDrivingOptionCode()))
        );
        // 新增Rule
        ConfigurationRuleAggr addRule = ConfigurationRuleFactory.createWithOptionList(context.getRuleGroup().getPurpose(), context.getRuleGroup().getUpdateUser(),
                editRule.getRuleOptionList());
        addRule.add();
        context.getAddRuleList().add(addRule);
    }

    /**
     * 预处理Rule更新（存在In Work状态的Rule、打点不为空）
     */
    private void preHandleRuleUpdateWithEdit(EditConfigurationRuleContext context, EditConfigurationRuleContext.EditConfigurationRule editRule) {
        if (!(Objects.nonNull(editRule.getInWorkRule()) && !editRule.isOptionEmptyOrAllUnavailable())) {
            return;
        }
        // 编辑打点
        ConfigurationRuleAggr updateRule = editRule.getInWorkRule();
        if (updateRule.updateOption(editRule.getRuleOptionList())) {
            context.getUpdateRuleList().add(updateRule);
        }
    }

    /**
     * 预处理Rule删除（存在In Work状态的Rule、打点为空）
     */
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
        // 校验Driving Feature和Constrained Feature
        checkDrivingAndConstrainedFeature(context.getRuleGroup(), context.getAddOrUpdateRuleList());
        // 校验Rule Driving下的Constrained打点不重复
        String message = configurationRuleDomainService.checkRuleDrivingConstrainedRepeat(context.getAddOrUpdateRuleList());
        if (StringUtils.isNotBlank(message)) {
            context.getErrorMessageList().add(0, message);
        }
        if (CollectionUtils.isNotEmpty(context.getErrorMessageList())) {
            return;
        }
        // 校验并处理新增的Rule
        checkAndProcessAddRule(context);
    }

    /**
     * 校验并处理新增的Rule
     */
    private void checkAndProcessAddRule(EditConfigurationRuleContext context) {
        List<ConfigurationRuleAggr> addRuleList = context.getAddRuleList();
        // 处理双向Rule
        addRuleList = configurationRuleDomainService.handleBothWayRule(addRuleList);
        // 分配Rule Number
        configurationRuleDomainService.generateRuleNumber(addRuleList);
        context.setAddRuleList(addRuleList);
    }

    /**
     * 校验并处理删除的Rule
     */
    private void checkAndProcessDeleteRule(EditConfigurationRuleContext context) {
        List<ConfigurationRuleAggr> deleteRuleList = context.getDeleteRuleList();
        if (CollectionUtils.isEmpty(deleteRuleList)) {
            return;
        }
        if (context.getRuleGroup().getRulePurposeEnum().isBothWay()) {
            // 处理双向Rule删除
            List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(context.getRuleGroup().getId());
            deleteRuleList.forEach(deleteRule -> {
                ConfigurationRuleAggr anotherDeleteRuleAggr = configurationRuleDomainService.findAnotherBothWayRule(deleteRule, ruleAggrList);
                anotherDeleteRuleAggr.delete();
                context.getDeleteRuleList().add(anotherDeleteRuleAggr);
            });
        }
    }

    @Override
    public void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList) {
        Set<String> featureCodeSet = Sets.newHashSet();
        // Group选择的Driving Feature和Constrained Feature
        Optional.ofNullable(ruleGroupAggr.getDrivingFeature()).ifPresent(featureCodeSet::add);
        Optional.ofNullable(ruleGroupAggr.getConstrainedFeatureList()).ifPresent(featureCodeSet::addAll);
        // 矩阵打点的Driving Feature和Constrained Feature
        List<ConfigurationRuleOptionDo> optionList = ruleAggrList.stream().flatMap(ruleAggr -> ruleAggr.getOptionList().stream())
                .filter(ConfigurationRuleOptionDo::isNotDeleted).toList();
        List<String> drivingFeatureCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getDrivingFeatureCode, true);
        List<String> constrainedFeatureCodeCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getConstrainedFeatureCode, true);
        if (drivingFeatureCodeList.size() > 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_DRIVING_FEATURE_ONLY_SELECT_ONE);
        }
        // 校验Group选择的Driving Feature和矩阵打点的Driving Feature必须一致
        if (StringUtils.isNotBlank(ruleGroupAggr.getDrivingFeature()) && drivingFeatureCodeList.size() > 0 &&
                !Objects.equals(ruleGroupAggr.getDrivingFeature(), drivingFeatureCodeList.get(0))) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_DRIVING_FEATURE_NOT_THE_SAME);
        }
        featureCodeSet.addAll(drivingFeatureCodeList);
        featureCodeSet.addAll(constrainedFeatureCodeCodeList);
        if (CollectionUtils.isEmpty(featureCodeSet)) {
            return;
        }
        List<FeatureAggr> featureAggrList = featureRepository.queryByFeatureOptionCodeList(Lists.newArrayList(featureCodeSet));
        checkDrivingAndConstrainedFeature2(ruleGroupAggr.getRulePurposeEnum(), ruleGroupAggr.getDrivingFeature(), ruleGroupAggr.getConstrainedFeatureList(), featureAggrList);
        checkDrivingAndConstrainedFeature2(ruleGroupAggr.getRulePurposeEnum(), drivingFeatureCodeList.size() > 0 ? drivingFeatureCodeList.get(0) : null,
                constrainedFeatureCodeCodeList, featureAggrList);
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

    /**
     * 校验Feature的Catalog
     */
    private void checkFeatureCatalog(FeatureAggr featureAggr, FeatureCatalogEnum catalogEnum) {
        if (featureAggr == null || !featureAggr.isFeature()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        if (!featureAggr.isCatalog(catalogEnum)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_FEATURE_CATALOG_NOT_MATCH);
        }
    }

}
