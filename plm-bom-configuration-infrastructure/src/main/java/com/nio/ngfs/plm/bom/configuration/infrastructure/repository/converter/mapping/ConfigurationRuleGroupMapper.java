package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleGroupMapper extends MapstructMapper<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> {

    ConfigurationRuleGroupMapper INSTANCE = Mappers.getMapper(ConfigurationRuleGroupMapper.class);

}
