package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.generator.RuleNumberGenerator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ConfigurationRuleConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ConfigurationRuleOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final RuleNumberGenerator ruleNumberGenerator;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<ConfigurationRuleAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(bomsConfigurationRuleDao, configurationRuleConverter, aggrList);
        aggrList.forEach(rule -> rule.getOptionList().forEach(option -> option.setRuleId(rule.getId())));
        List<ConfigurationRuleOptionDo> optionList = aggrList.stream().flatMap(i -> i.getOptionList().stream()).toList();
        DaoSupport.batchSaveOrUpdate(bomsConfigurationRuleOptionDao, configurationRuleOptionConverter.convertDoListToEntityList(optionList));
    }

    @Override
    public List<String> applyRuleNumber(int size) {
        return ruleNumberGenerator.applyRuleNumber(size);
    }

}
