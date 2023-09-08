package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductConfigOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Repository
@RequiredArgsConstructor
public class ProductConfigOptionRepositoryImpl implements ProductConfigOptionRepository {

    private final BomsProductConfigOptionDao bomsProductConfigOptionDao;
    private final ProductConfigOptionConverter productConfigOptionConverter;

    @Override
    public void save(ProductConfigOptionAggr aggr) {
        DaoSupport.saveOrUpdate(bomsProductConfigOptionDao, productConfigOptionConverter.convertDoToEntity(aggr));
    }

    @Override
    public ProductConfigOptionAggr find(ProductConfigOptionId id) {
        return productConfigOptionConverter.convertEntityToDo(
                bomsProductConfigOptionDao.getByPcIdAndOptionCode(id.getPcId(), id.getOptionCode())
        );
    }

    @Override
    public List<ProductConfigOptionAggr> queryByPcId(Long pcId) {
        return productConfigOptionConverter.convertEntityListToDoList(
                bomsProductConfigOptionDao.queryByPcId(pcId)
        );
    }

    @Override
    public void batchSave(List<ProductConfigOptionAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(bomsProductConfigOptionDao, productConfigOptionConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public List<ProductConfigOptionAggr> queryByPcIdList(List<Long> pcIdList) {
        return productConfigOptionConverter.convertEntityListToDoList(
                bomsProductConfigOptionDao.queryByPcIdList(pcIdList, null)
        );
    }

}
