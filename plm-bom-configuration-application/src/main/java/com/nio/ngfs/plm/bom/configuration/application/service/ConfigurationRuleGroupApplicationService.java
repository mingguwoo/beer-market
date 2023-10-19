package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleGroupApplicationService {

    /**
     * 校验Driving Feature和Constrained Feature
     *
     * @param aggr 聚合根
     */
    void checkDrivingAndConstrainedFeature(ConfigurationRuleGroupAggr aggr);

}
