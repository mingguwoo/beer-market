package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductConfigModelOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Repository
@RequiredArgsConstructor
public class ProductConfigModelOptionRepositoryImpl implements ProductConfigModelOptionRepository {

    private final BomsProductConfigModelOptionDao productConfigModelOptionDao;
    private final ProductConfigModelOptionConverter productConfigModelOptionConverter;

    @Override
    public void save(ProductConfigModelOptionAggr aggr) {
        DaoSupport.saveOrUpdate(productConfigModelOptionDao, productConfigModelOptionConverter.convertDoToEntity(aggr));
    }

    @Override
    public ProductConfigModelOptionAggr find(ProductConfigModelOptionId id) {
        return productConfigModelOptionConverter.convertEntityToDo(
                productConfigModelOptionDao.getByModelAndOptionCode(id.getModelCode(), id.getOptionCode())
        );
    }

    @Override
    public List<ProductConfigModelOptionAggr> queryByModel(String modelCode) {
        return productConfigModelOptionConverter.convertEntityListToDoList(
                productConfigModelOptionDao.queryByModel(modelCode)
        );
    }

    @Override
    public void batchSave(List<ProductConfigModelOptionAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(productConfigModelOptionDao, productConfigModelOptionConverter.convertDoListToEntityList(aggrList));
    }

}
