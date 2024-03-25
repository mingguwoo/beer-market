package com.sh.beer.market.infrastructure.repository.converter;


import com.sh.beer.market.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.sh.beer.market.infrastructure.repository.converter.common.MapStructDataConverter;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleGroupConverter implements MapStructDataConverter<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> {

    /*@Override
    public MapstructMapper<ConfigurationRuleGroupAggr, BomsConfigurationRuleGroupEntity> getMapper() {
        return ConfigurationRuleGroupMapper.INSTANCE;
    }*/

}
