package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
@RequiredArgsConstructor
public class FeatureRepositoryImpl implements FeatureRepository {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final FeatureConverter featureConverter;

    @Override
    public void save(FeatureAggr aggr) {
        DaoSupport.saveOrUpdate(bomsFeatureLibraryDao, featureConverter.convertDoToEntity(aggr));
    }

    @Override
    public FeatureAggr find(Long id) {
        return featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getById(id));
    }

    @Override
    public FeatureAggr getByFeatureCodeAndType(String featureCode, String type) {
        return featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getByFeatureCodeAndType(featureCode, type));
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByParentFeatureCode(parentFeatureCode));
    }

    @Override
    public List<FeatureAggr> queryAll() {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryAll());
    }

}
