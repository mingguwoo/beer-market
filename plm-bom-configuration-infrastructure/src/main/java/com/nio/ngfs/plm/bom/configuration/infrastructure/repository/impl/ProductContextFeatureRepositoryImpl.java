package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductContextFeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Repository
@RequiredArgsConstructor
public class ProductContextFeatureRepositoryImpl implements ProductContextFeatureRepository {

    private ProductContextFeatureConverter productContextFeatureConverter;
    private BomsProductContextFeatureDao productContextFeatureDao;

    @Override
    public void save(ProductContextFeatureAggr productContextFeatureAggr) {
        DaoSupport.saveOrUpdate(productContextFeatureDao,productContextFeatureConverter.convertDoToEntity(productContextFeatureAggr));
    }

    @Override
    public ProductContextFeatureAggr find(Long aLong) {
        return null;
    }

    @Override
    public void batchSave(List<ProductContextFeatureAggr> productContextFeatureAggrList) {
        productContextFeatureDao.saveBatch(productContextFeatureConverter.convertDoListToEntityList(productContextFeatureAggrList));
    }

    @Override
    public List<ProductContextFeatureAggr> queryByModelCode(String modelCode) {
        return productContextFeatureConverter.convertEntityListToDoList(productContextFeatureDao.queryByModelCode(modelCode));
    }
}
