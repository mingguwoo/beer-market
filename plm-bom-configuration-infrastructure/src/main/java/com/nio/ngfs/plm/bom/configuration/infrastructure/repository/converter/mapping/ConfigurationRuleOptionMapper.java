package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleOptionMapper extends MapstructMapper<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> {

    ConfigurationRuleOptionMapper INSTANCE = Mappers.getMapper(ConfigurationRuleOptionMapper.class);

}
