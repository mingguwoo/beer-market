package com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleGroupDomainService {

    /**
     * 校验Defined By
     *
     * @param aggr 聚合根
     */
    void checkDefinedBy(ConfigurationRuleGroupAggr aggr);

}
