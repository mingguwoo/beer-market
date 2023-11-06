package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface ConfigurationRuleGroupRepository extends Repository<ConfigurationRuleGroupAggr, Long> {





      List<ConfigurationRuleGroupAggr> queryConfigurationRuleGroupsByDefinedBy(List<String> definedBy);
}
