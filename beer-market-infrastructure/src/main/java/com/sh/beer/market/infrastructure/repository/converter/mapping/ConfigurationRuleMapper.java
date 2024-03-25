package com.sh.beer.market.infrastructure.repository.converter.mapping;


import com.sh.beer.market.domain.model.configurationrule.ConfigurationRuleAggr;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleMapper extends MapstructMapper<ConfigurationRuleAggr, BomsConfigurationRuleEntity> {

    ConfigurationRuleMapper INSTANCE = Mappers.getMapper(ConfigurationRuleMapper.class);

}
