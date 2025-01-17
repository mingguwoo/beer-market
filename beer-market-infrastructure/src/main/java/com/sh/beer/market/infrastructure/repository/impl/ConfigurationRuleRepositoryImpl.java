package com.sh.beer.market.infrastructure.repository.impl;

import com.sh.beer.market.domain.model.configurationrule.ConfigurationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/10/17
 */
@Repository
@RequiredArgsConstructor
public class ConfigurationRuleRepositoryImpl implements ConfigurationRuleRepository {

    /*private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final BomsConfigurationRuleOptionDao bomsConfigurationRuleOptionDao;
    private final ConfigurationRuleConverter configurationRuleConverter;
    private final ConfigurationRuleOptionConverter configurationRuleOptionConverter;
    private final RuleNumberGenerator ruleNumberGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ConfigurationRuleAggr aggr) {
        DaoSupport.saveOrUpdate(bomsConfigurationRuleDao, configurationRuleConverter.convertDoToEntity(aggr), entity -> {
            if (aggr.getId() == null) {
                aggr.setId(entity.getId());
            }
        });
        aggr.getOptionList().forEach(option -> option.setRuleId(aggr.getId()));
        bomsConfigurationRuleOptionDao.saveOrUpdateBatch(
                configurationRuleOptionConverter.convertDoListToEntityList(aggr.getOptionList())
        );
    }

    @Override
    public ConfigurationRuleAggr find(Long id) {
        ConfigurationRuleAggr aggr = configurationRuleConverter.convertEntityToDo(bomsConfigurationRuleDao.getById(id));
        if (aggr != null) {
            queryAndBuildRuleOptionList(Lists.newArrayList(aggr));
        }
        return aggr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<ConfigurationRuleAggr> aggrList) {
        if (CollectionUtils.isEmpty(aggrList)) {
            return;
        }
        DaoSupport.batchSaveOrUpdate(bomsConfigurationRuleDao, configurationRuleConverter, aggrList);
        aggrList.forEach(rule -> rule.getOptionList().stream().filter(option -> Objects.isNull(option.getRuleId())).forEach(option -> option.setRuleId(rule.getId())));
        List<ConfigurationRuleOptionDo> optionList = aggrList.stream().flatMap(i -> i.getOptionList().stream()).toList();
        List<ConfigurationRuleOptionDo> saveOrUpdateOptionList = optionList.stream().filter(ConfigurationRuleOptionDo::isNotDeleted).toList();
        List<ConfigurationRuleOptionDo> deleteOptionList = optionList.stream().filter(ConfigurationRuleOptionDo::isDeleted).toList();
        if (CollectionUtils.isNotEmpty(saveOrUpdateOptionList)) {
            DaoSupport.batchSaveOrUpdate(bomsConfigurationRuleOptionDao, configurationRuleOptionConverter.convertDoListToEntityList(saveOrUpdateOptionList));
        }
        if (CollectionUtils.isNotEmpty(deleteOptionList)) {
            bomsConfigurationRuleOptionDao.removeBatchByIds(LambdaUtil.map(deleteOptionList, ConfigurationRuleOptionDo::getId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<ConfigurationRuleAggr> aggrList) {
        if (CollectionUtils.isEmpty(aggrList)) {
            return;
        }
        bomsConfigurationRuleDao.removeBatchByIds(LambdaUtil.map(aggrList, ConfigurationRuleAggr::getId));
        bomsConfigurationRuleOptionDao.removeBatchByIds(aggrList.stream()
                .flatMap(aggr -> LambdaUtil.map(aggr.getOptionList(), ConfigurationRuleOptionDo::getId).stream()).toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<ConfigurationRuleAggr> aggrList) {
        bomsConfigurationRuleDao.updateBatchById(configurationRuleConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public List<ConfigurationRuleAggr> queryByGroupId(Long groupId) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleConverter.convertEntityListToDoList(
                bomsConfigurationRuleDao.queryByGroupId(groupId)
        );
        return queryAndBuildRuleOptionList(ruleAggrList);
    }

    @Override
    public List<ConfigurationRuleAggr> queryByIdsAndStatus(List<Long> groupIds, String status, boolean optionFlag) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleConverter.convertEntityListToDoList(bomsConfigurationRuleDao.queryByGroupIdsAndStatus(groupIds,status));
        if (optionFlag) {
            return queryAndBuildRuleOptionList(ruleAggrList);
        }
        return ruleAggrList;
    }

    @Override
    public List<ConfigurationRuleAggr> queryByGroupIdList(List<Long> groupIdList) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleConverter.convertEntityListToDoList(
                bomsConfigurationRuleDao.queryByGroupIdList(groupIdList)
        );
        return queryAndBuildRuleOptionList(ruleAggrList);
    }

    @Override
    public List<ConfigurationRuleAggr> queryByRuleIdList(List<Long> ruleIdList, boolean optionFlag) {
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleConverter.convertEntityListToDoList(
                bomsConfigurationRuleDao.listByIds(ruleIdList)
        );
        if (optionFlag) {
            return queryAndBuildRuleOptionList(ruleAggrList);
        }
        return ruleAggrList;
    }

    @Override
    public List<ConfigurationRuleAggr> queryByRuleNumbers(List<String> ruleNumbers) {
        return configurationRuleConverter.convertEntityListToDoList(bomsConfigurationRuleDao.queryByRuleNumbers(ruleNumbers));
    }

    private List<ConfigurationRuleAggr> queryAndBuildRuleOptionList(List<ConfigurationRuleAggr> ruleAggrList) {
        List<ConfigurationRuleOptionDo> ruleOptionDoList = configurationRuleOptionConverter.convertEntityListToDoList(
                bomsConfigurationRuleOptionDao.queryByRuleIdList(LambdaUtil.map(ruleAggrList, ConfigurationRuleAggr::getId))
        );
        Map<Long, List<ConfigurationRuleOptionDo>> optionListMap = LambdaUtil.groupBy(ruleOptionDoList, ConfigurationRuleOptionDo::getRuleId);
        ruleAggrList.forEach(ruleAggr -> {
            ruleAggr.setOptionList(optionListMap.getOrDefault(ruleAggr.getId(), Lists.newArrayList()));
            ruleAggr.getOptionList().forEach(option -> option.setRule(ruleAggr));
        });
        return ruleAggrList;
    }

    @Override
    public List<String> applyRuleNumber(int size) {
        return ruleNumberGenerator.applyRuleNumber(size);
    }
*/
}
