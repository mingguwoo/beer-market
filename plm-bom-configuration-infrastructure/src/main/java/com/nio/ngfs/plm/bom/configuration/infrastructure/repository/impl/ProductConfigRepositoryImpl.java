package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductConfigConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Repository
@RequiredArgsConstructor
public class ProductConfigRepositoryImpl implements ProductConfigRepository {

    private final BomsProductConfigDao bomsProductConfigDao;
    private final ProductConfigConverter productConfigConverter;

    @Override
    public void save(ProductConfigAggr aggr) {
        DaoSupport.saveOrUpdate(bomsProductConfigDao, productConfigConverter.convertDoToEntity(aggr));
    }

    @Override
    public ProductConfigAggr find(String pcId) {
        return productConfigConverter.convertEntityToDo(bomsProductConfigDao.getByPcId(pcId));
    }

    @Override
    public void remove(ProductConfigAggr aggr) {
        bomsProductConfigDao.remove(productConfigConverter.convertDoToEntity(aggr));
    }

    @Override
    public ProductConfigAggr queryLastPcByModelAndModelYear(String modelCode, String modelYear) {
        return productConfigConverter.convertEntityToDo(bomsProductConfigDao.queryLastPcByModelAndModelYear(modelCode, modelYear));
    }

    @Override
    public ProductConfigAggr getByName(String name) {
        return productConfigConverter.convertEntityToDo(bomsProductConfigDao.getByName(name));
    }

    @Override
    public List<ProductConfigAggr> queryByPcIdList(List<String> pcIdList) {
        return productConfigConverter.convertEntityListToDoList(bomsProductConfigDao.queryByPcIdList(pcIdList));
    }

    @Override
    public void batchSave(List<ProductConfigAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(bomsProductConfigDao, productConfigConverter.convertDoListToEntityList(aggrList));
    }

}
