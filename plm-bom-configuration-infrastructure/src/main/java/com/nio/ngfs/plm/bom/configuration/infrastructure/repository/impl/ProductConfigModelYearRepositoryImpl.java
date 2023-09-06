package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ProductConfigModelYearConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelYearDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Repository
@RequiredArgsConstructor
public class ProductConfigModelYearRepositoryImpl implements ProductConfigModelYearRepository {

    private final BomsProductConfigModelYearDao productConfigModelYearDao;
    private final ProductConfigModelYearConverter productConfigModelYearConverter;

    @Override
    public void save(ProductConfigModelYearAggr aggr) {
        DaoSupport.saveOrUpdate(productConfigModelYearDao, productConfigModelYearConverter.convertDoToEntity(aggr));
    }

    @Override
    public ProductConfigModelYearAggr find(ProductConfigModelYearId id) {
        return productConfigModelYearConverter.convertEntityToDo(productConfigModelYearDao.getModelYearConfig(
                id.getModel(), id.getModelYear()
        ));
    }

    @Override
    public void batchSave(List<ProductConfigModelYearAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(productConfigModelYearDao, productConfigModelYearConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public List<ProductConfigModelYearAggr> queryByModel(String model) {
        return productConfigModelYearConverter.convertEntityListToDoList(productConfigModelYearDao.queryByModel(model));
    }

}
