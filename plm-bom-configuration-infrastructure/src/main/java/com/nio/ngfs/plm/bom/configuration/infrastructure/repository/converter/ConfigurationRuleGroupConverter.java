package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ConfigurationRuleGroupMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleGroupConverter implements MapStructDataConverter<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> {

    @Override
    public MapstructMapper<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> getMapper() {
        return ConfigurationRuleGroupMapper.INSTANCE;
    }

    @Override
    public void convertDoToEntityCallback(ConfigurationRuleGroupAggr aggr, BomsConfigurationRuleGroupEntity entity) {
        entity.setConstrainedFeatureList(Joiner.on(",").skipNulls().join(aggr.getConstrainedFeatureList()));
    }

    @Override
    public void convertEntityToDoCallback(BomsConfigurationRuleGroupEntity entity, ConfigurationRuleGroupAggr aggr) {
        aggr.setConstrainedFeatureList(Splitter.on(",").trimResults().omitEmptyStrings().splitToList(entity.getConstrainedFeatureList()));
    }

}
