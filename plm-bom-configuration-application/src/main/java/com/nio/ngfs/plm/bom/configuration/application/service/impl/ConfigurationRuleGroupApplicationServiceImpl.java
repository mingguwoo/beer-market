package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleGroupApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleGroupApplicationServiceImpl implements ConfigurationRuleGroupApplicationService {

    private final FeatureRepository featureRepository;

    @Override
    public void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr aggr) {
        List<String> featureCodeList = Lists.newArrayList();
        Optional.ofNullable(aggr.getDrivingFeature()).ifPresent(featureCodeList::add);
        Optional.ofNullable(aggr.getConstrainedFeatureList()).ifPresent(featureCodeList::addAll);
        if (CollectionUtils.isEmpty(featureCodeList)) {
            return;
        }
        List<FeatureAggr> featureAggrList = featureRepository.queryByFeatureOptionCodeList(featureCodeList);
        Map<String, FeatureAggr> featureAggrMap = LambdaUtil.toKeyMap(featureAggrList, FeatureAggr::getFeatureCode);
        // 校验Driving Feature
        if (StringUtils.isNotBlank(aggr.getDrivingFeature())) {
            checkFeatureCatalog(featureAggrMap.get(aggr.getDrivingFeature()), FeatureCatalogEnum.SALES);
        }
        // 校验Constrained Feature
        if (CollectionUtils.isNotEmpty(aggr.getConstrainedFeatureList())) {
            if (!aggr.isPurpose(ConfigurationRulePurposeEnum.SALES_TO_ENG) && !aggr.isPurpose(ConfigurationRulePurposeEnum.SALES_TO_SALES)) {
                if (aggr.getConstrainedFeatureList().size() > 1) {
                    throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_CONSTRAINED_FEATURE_ONLY_SELECT_ONE);
                }
            }
            aggr.getConstrainedFeatureList().forEach(constrainedFeature -> {
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
        if (featureAggr == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        if (!featureAggr.isCatalog(catalogEnum)) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_FEATURE_CATALOG_NOT_MATCH);
        }
    }

}
