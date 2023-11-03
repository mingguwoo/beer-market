package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ConfigurationRuleOptionMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleOptionConverter implements MapStructDataConverter<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> {

    @Override
    public MapstructMapper<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> getMapper() {
        return ConfigurationRuleOptionMapper.INSTANCE;
    }

}
