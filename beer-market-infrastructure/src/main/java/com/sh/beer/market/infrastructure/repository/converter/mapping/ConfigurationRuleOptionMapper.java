package com.sh.beer.market.infrastructure.repository.converter.mapping;


import com.sh.beer.market.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @date 2023/10/17
 */
@Mapper
public interface ConfigurationRuleOptionMapper extends MapstructMapper<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> {

    ConfigurationRuleOptionMapper INSTANCE = Mappers.getMapper(ConfigurationRuleOptionMapper.class);

}
