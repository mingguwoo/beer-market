package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ConfigurationRuleConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ConfigurationRuleOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Repository
@RequiredArgsConstructor
public class ConfigurationRuleRepositoryImpl implements ConfigurationRuleRepository {

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final BomsConfigurationRuleOptionDao bomsConfigurationRuleOptionDao;
    private final ConfigurationRuleConverter configurationRuleConverter;
    private final ConfigurationRuleOptionConverter configurationRuleOptionConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ConfigurationRuleAggr aggr) {
        DaoSupport.saveOrUpdate(bomsConfigurationRuleDao, configurationRuleConverter.convertDoToEntity(aggr));
        bomsConfigurationRuleOptionDao.saveOrUpdateBatch(
                configurationRuleOptionConverter.convertDoListToEntityList(aggr.getOptionList())
        );
    }

    @Override
    public ConfigurationRuleAggr find(Long id) {
        ConfigurationRuleAggr aggr = configurationRuleConverter.convertEntityToDo(bomsConfigurationRuleDao.getById(id));
        if (aggr != null) {
            aggr.setOptionList(configurationRuleOptionConverter.convertEntityListToDoList(
                    bomsConfigurationRuleOptionDao.queryByRuleId(aggr.getId())
            ));
        }
        return aggr;
    }

}
