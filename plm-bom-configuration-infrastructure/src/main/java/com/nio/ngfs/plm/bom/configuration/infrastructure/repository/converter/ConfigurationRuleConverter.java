package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ConfigurationRuleMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleConverter implements MapStructDataConverter<ConfigurationRuleAggr, BomsConfigurationRuleEntity> {

    @Override
    public MapstructMapper<ConfigurationRuleAggr, BomsConfigurationRuleEntity> getMapper() {
        return ConfigurationRuleMapper.INSTANCE;
    }

    @Override
    public void convertEntityToDoCallback(BomsConfigurationRuleEntity entity, ConfigurationRuleAggr domainObject) {
        domainObject.setOptionList(Lists.newArrayList());
    }

}
