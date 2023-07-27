package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoVersionSnapshotConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.OxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoVersionSnapshotRepositoryImpl implements OxoVersionSnapshotRepository {

    private final OxoVersionSnapshotDao oxoVersionSnapshotDao;
    private final OxoVersionSnapshotConverter oxoVersionSnapshotConverter;

    @Override
    public void save(OxoVersionSnapshotAggr aggr) {
        DaoSupport.saveOrUpdate(oxoVersionSnapshotDao, oxoVersionSnapshotConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoVersionSnapshotAggr find(Long id) {
        return oxoVersionSnapshotConverter.convertEntityToDo(oxoVersionSnapshotDao.getById(id));
    }

}
