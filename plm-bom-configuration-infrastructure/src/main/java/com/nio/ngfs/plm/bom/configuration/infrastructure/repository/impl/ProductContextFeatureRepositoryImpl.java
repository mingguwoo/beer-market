package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductContextFeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
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

    private final ProductContextFeatureConverter productContextFeatureConverter;
    private final BomsProductContextFeatureDao productContextFeatureDao;

    @Override
    public void save(ProductContextFeatureAggr productContextFeatureAggr) {
        DaoSupport.saveOrUpdate(productContextFeatureDao,productContextFeatureConverter.convertDoToEntity(productContextFeatureAggr));
    }

    @Override
    public ProductContextFeatureAggr find(Long id) {
        return null;
    }

    @Override
    public void saveBatch(List<ProductContextFeatureAggr> productContextFeatureAggrList) {
        productContextFeatureDao.saveOrUpdateBatch(productContextFeatureConverter.convertDoListToEntityList(productContextFeatureAggrList));
    }

    @Override
    public List<ProductContextFeatureAggr> queryByModelCode(String modelCode) {
        return productContextFeatureConverter.convertEntityListToDoList(productContextFeatureDao.queryByModelCode(modelCode));
    }

    @Override
    public List<ProductContextFeatureAggr> queryByModelCodeAndFeatureCode(String modelCode, List<String> featureCodeList) {
        return productContextFeatureConverter.convertEntityListToDoList(productContextFeatureDao.queryByModelCodeAndFeatureCode(modelCode, featureCodeList));
    }

    @Override
    public List<ProductContextFeatureAggr> queryAll() {
        return productContextFeatureConverter.convertEntityListToDoList(productContextFeatureDao.queryAll());
    }
}
