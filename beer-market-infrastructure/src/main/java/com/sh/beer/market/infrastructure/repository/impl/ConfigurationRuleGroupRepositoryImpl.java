package com.sh.beer.market.infrastructure.repository.impl;

import com.sh.beer.market.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/10/17
 */
@Repository
@RequiredArgsConstructor
public class ConfigurationRuleGroupRepositoryImpl implements ConfigurationRuleGroupRepository {

    /*private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final ConfigurationRuleGroupConverter configurationRuleGroupConverter;

    @Override
    public void save(ConfigurationRuleGroupAggr aggr) {
        DaoSupport.saveOrUpdate(bomsConfigurationRuleGroupDao, configurationRuleGroupConverter.convertDoToEntity(aggr), entity -> {
            if (aggr.getId() == null) {
                aggr.setId(entity.getId());
            }
        });
    }

    @Override
    public ConfigurationRuleGroupAggr find(Long id) {
        return configurationRuleGroupConverter.convertEntityToDo(bomsConfigurationRuleGroupDao.getById(id));
    }

    @Override
    public void remove(ConfigurationRuleGroupAggr aggr) {
        bomsConfigurationRuleGroupDao.removeById(aggr.getId());
    }

    @Override
    public List<ConfigurationRuleGroupAggr> queryConfigurationRuleGroupsByDefinedBy(List<String> definedBy) {
      return  configurationRuleGroupConverter.convertEntityListToDoList(bomsConfigurationRuleGroupDao.queryByDefinedBy(definedBy));
    }*/
}
