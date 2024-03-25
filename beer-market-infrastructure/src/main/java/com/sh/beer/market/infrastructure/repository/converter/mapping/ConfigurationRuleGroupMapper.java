package com.sh.beer.market.infrastructure.repository.converter.mapping;


import com.sh.beer.market.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleGroupMapper extends MapstructMapper<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> {

    ConfigurationRuleGroupMapper INSTANCE = Mappers.getMapper(ConfigurationRuleGroupMapper.class);

}
