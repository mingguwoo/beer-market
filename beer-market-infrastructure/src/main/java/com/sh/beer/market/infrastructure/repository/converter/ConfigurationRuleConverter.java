package com.sh.beer.market.infrastructure.repository.converter;


import com.sh.beer.market.domain.model.configurationrule.ConfigurationRuleAggr;
import com.sh.beer.market.infrastructure.repository.converter.common.MapStructDataConverter;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/10/17
 */
@Component
public class ConfigurationRuleConverter implements MapStructDataConverter<ConfigurationRuleAggr, BomsConfigurationRuleEntity> {

    /*@Override
    public MapstructMapper<ConfigurationRuleAggr, BomsConfigurationRuleEntity> getMapper() {
        return ConfigurationRuleMapper.INSTANCE;
    }

    @Override
    public void convertEntityToDoCallback(BomsConfigurationRuleEntity entity, ConfigurationRuleAggr domainObject) {
        domainObject.setOptionList(Lists.newArrayList());
    }*/

}
