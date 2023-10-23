package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ConfigurationRuleGroupConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Repository
@RequiredArgsConstructor
public class ConfigurationRuleGroupRepositoryImpl implements ConfigurationRuleGroupRepository {

    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
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

}
