package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductContextConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Repository
@RequiredArgsConstructor
public class ProductContextRepositoryImpl implements ProductContextRepository {

    private final BomsProductContextDao bomsProductContextDao;
    private final ProductContextConverter productContextConverter;

    public List<ProductContextAggr> queryByModelCode(String modelCode){
        return productContextConverter.convertEntityListToDoList(bomsProductContextDao.queryByModelCode(modelCode));
    }

    @Override
    public void save(ProductContextAggr productContextAggr) {
        DaoSupport.saveOrUpdate(bomsProductContextDao,productContextConverter.convertDoToEntity(productContextAggr));
    }

    @Override
    public ProductContextAggr find(Long aLong) {
        return null;
    }
}
