package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleMapper extends MapstructMapper<ConfigurationRuleAggr, BomsConfigurationRuleEntity> {

    ConfigurationRuleMapper INSTANCE = Mappers.getMapper(ConfigurationRuleMapper.class);

}
