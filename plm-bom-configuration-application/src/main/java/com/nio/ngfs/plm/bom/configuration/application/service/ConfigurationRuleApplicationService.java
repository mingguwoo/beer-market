package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.context.EditConfigurationRuleContext;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleApplicationService {

    /**
     * 构建编辑Rule上下文
     *
     * @param ruleAggrList     Rule列表
     * @param ruleOptionDoList Rule打点列表
     * @return 编辑Rule上下文
     */
    EditConfigurationRuleContext buildEditConfigurationRuleContext(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList,
                                                                   List<ConfigurationRuleOptionDo> ruleOptionDoList);


    /**
     * 校验并处理Rule编辑
     *
     * @param context 上下文
     */
    void checkAndProcessEditRule(EditConfigurationRuleContext context);

    /**
     * 校验Driving Feature和Constrained Feature
     *
     * @param purposeEnum            Purpose
     * @param drivingFeature         Driving Feature
     * @param constrainedFeatureList Constrained Feature列表
     * @param ruleAggrList           聚合根列表
     */
    void checkDrivingAndConstrainedFeature(ConfigurationRulePurposeEnum purposeEnum, String drivingFeature,
                                           List<String> constrainedFeatureList, List<ConfigurationRuleAggr> ruleAggrList);

}
