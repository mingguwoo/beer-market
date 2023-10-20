package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

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

}
