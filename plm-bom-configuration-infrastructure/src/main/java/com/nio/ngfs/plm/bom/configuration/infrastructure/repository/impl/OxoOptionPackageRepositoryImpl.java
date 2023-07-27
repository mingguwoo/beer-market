package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoOptionPackageConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.OxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoOptionPackageRepositoryImpl implements OxoOptionPackageRepository {

    private final OxoOptionPackageDao oxoOptionPackageDao;
    private final OxoOptionPackageConverter oxoOptionPackageConverter;

    @Override
    public void save(OxoOptionPackageAggr aggr) {
        DaoSupport.saveOrUpdate(oxoOptionPackageDao, oxoOptionPackageConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoOptionPackageAggr find(Long id) {
        return oxoOptionPackageConverter.convertEntityToDo(oxoOptionPackageDao.getById(id));
    }

}
