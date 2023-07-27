package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoFeatureOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.OxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoFeatureOptionRepositoryImpl implements OxoFeatureOptionRepository {

    private final OxoFeatureOptionDao oxoFeatureOptionDao;
    private final OxoFeatureOptionConverter oxoFeatureOptionConverter;

    @Override
    public void save(OxoFeatureOptionAggr aggr) {
        DaoSupport.saveOrUpdate(oxoFeatureOptionDao, oxoFeatureOptionConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoFeatureOptionAggr find(Long id) {
        return oxoFeatureOptionConverter.convertEntityToDo(oxoFeatureOptionDao.getById(id));
    }

}
