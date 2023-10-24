package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleGroupApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
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
public class ConfigurationRuleGroupApplicationServiceImpl implements ConfigurationRuleGroupApplicationService {

    private final FeatureRepository featureRepository;

    @Override
    public void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr aggr, List<ConfigurationRuleAggr> ruleAggrList) {
        Set<String> featureCodeSet = Sets.newHashSet();
        // Group选择的Driving Feature和Constrained Feature
        Optional.ofNullable(aggr.getDrivingFeature()).ifPresent(featureCodeSet::add);
        Optional.ofNullable(aggr.getConstrainedFeatureList()).ifPresent(featureCodeSet::addAll);
        // 矩阵打点的Driving Feature和Constrained Feature
        List<ConfigurationRuleOptionDo> optionList = ruleAggrList.stream().flatMap(ruleAggr -> ruleAggr.getOptionList().stream()).toList();
        List<String> drivingFeatureCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getDrivingFeatureCode, true);
        List<String> constrainedFeatureCodeCodeList = LambdaUtil.map(optionList, ConfigurationRuleOptionDo::getConstrainedFeatureCode, true);
        if (drivingFeatureCodeList.size() > 1) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_BOTH_WAY_DRIVING_FEATURE_ONLY_SELECT_ONE);
        }
        featureCodeSet.addAll(drivingFeatureCodeList);
        featureCodeSet.addAll(constrainedFeatureCodeCodeList);
        if (CollectionUtils.isEmpty(featureCodeSet)) {
            return;
        }
        List<FeatureAggr> featureAggrList = featureRepository.queryByFeatureOptionCodeList(Lists.newArrayList(featureCodeSet));
        checkDrivingAndConstrainedFeature(aggr.getDrivingFeature(), aggr.getConstrainedFeatureList(), featureAggrList, aggr);
        checkDrivingAndConstrainedFeature(drivingFeatureCodeList.size() > 0 ? drivingFeatureCodeList.get(0) : null,
                constrainedFeatureCodeCodeList, featureAggrList, aggr);
    }

    private void checkDrivingAndConstrainedFeature(String drivingFeatureCode, List<String> constrainedFeatureCodeCodeList,
                                                   List<FeatureAggr> featureAggrList, ConfigurationRuleGroupAggr aggr) {
        Map<String, FeatureAggr> featureAggrMap = LambdaUtil.toKeyMap(featureAggrList, FeatureAggr::getFeatureCode);
        // 校验Driving Feature
        if (StringUtils.isNotBlank(drivingFeatureCode)) {
            checkFeatureCatalog(featureAggrMap.get(drivingFeatureCode), FeatureCatalogEnum.SALES);
        }
        // 校验Constrained Feature
        if (CollectionUtils.isNotEmpty(constrainedFeatureCodeCodeList)) {
            if (!aggr.isPurpose(ConfigurationRulePurposeEnum.SALES_TO_ENG) && !aggr.isPurpose(ConfigurationRulePurposeEnum.SALES_TO_SALES)) {
                if (constrainedFeatureCodeCodeList.size() > 1) {
                    throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_CONSTRAINED_FEATURE_ONLY_SELECT_ONE);
                }
            }
            constrainedFeatureCodeCodeList.forEach(constrainedFeature -> {
                if (aggr.isPurpose(ConfigurationRulePurposeEnum.SALES_TO_ENG)) {
                    checkFeatureCatalog(featureAggrMap.get(constrainedFeature), FeatureCatalogEnum.ENGINEERING);
                } else {
                    checkFeatureCatalog(featureAggrMap.get(constrainedFeature), FeatureCatalogEnum.SALES);
                }
            });
        }
    }

    @Override
    public void checkDeleteGroup(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList.forEach(ruleAggr -> {
            if (!(Objects.equals(ruleAggr.getRuleVersion(), ConfigConstants.VERSION_AA) &&
                    ruleAggr.isStatus(ConfigurationRuleStatusEnum.IN_WORK))) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_CAN_NOT_DELETE);
            }
        });
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
