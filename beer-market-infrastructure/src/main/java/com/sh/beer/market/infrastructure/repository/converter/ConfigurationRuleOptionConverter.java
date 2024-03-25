package com.sh.beer.market.infrastructure.repository.converter;


import com.sh.beer.market.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.sh.beer.market.infrastructure.repository.converter.common.MapStructDataConverter;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleOptionConverter implements MapStructDataConverter<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> {

   /* @Override
    public MapstructMapper<ConfigurationRuleOptionDo, BomsConfigurationRuleOptionEntity> getMapper() {
        return ConfigurationRuleOptionMapper.INSTANCE;
    }
*/
}
