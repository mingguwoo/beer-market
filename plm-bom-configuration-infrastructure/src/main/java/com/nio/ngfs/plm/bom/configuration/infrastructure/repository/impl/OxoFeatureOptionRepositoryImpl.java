package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoFeatureOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoFeatureOptionRepositoryImpl implements OxoFeatureOptionRepository {

    private final BomsOxoFeatureOptionDao bomsOxoFeatureOptionDao;
    private final OxoFeatureOptionConverter oxoFeatureOptionConverter;

    @Override
    public void save(OxoFeatureOptionAggr aggr) {
        DaoSupport.saveOrUpdate(bomsOxoFeatureOptionDao, oxoFeatureOptionConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoFeatureOptionAggr find(Long id) {
        return oxoFeatureOptionConverter.convertEntityToDo(bomsOxoFeatureOptionDao.getById(id));
    }

    @Override
    public List<OxoFeatureOptionAggr> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList) {
        return oxoFeatureOptionConverter.convertEntityListToDoList(bomsOxoFeatureOptionDao.queryByModelAndFeatureCodeList(model, featureCodeList));
    }

    @Override
    public void batchSave(List<OxoFeatureOptionAggr> aggrList) {
        bomsOxoFeatureOptionDao.updateBatchById(oxoFeatureOptionConverter.convertDoListToEntityList(aggrList));
    }

}
