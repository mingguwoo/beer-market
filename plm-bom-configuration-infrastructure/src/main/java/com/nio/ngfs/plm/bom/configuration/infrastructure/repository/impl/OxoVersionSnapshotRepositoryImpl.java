package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoVersionSnapshotConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoVersionSnapshotRepositoryImpl implements OxoVersionSnapshotRepository {

    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;
    private final OxoVersionSnapshotConverter oxoVersionSnapshotConverter;

    @Override
    public void save(OxoVersionSnapshotAggr aggr) {
        DaoSupport.saveOrUpdate(bomsOxoVersionSnapshotDao, oxoVersionSnapshotConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoVersionSnapshotAggr find(Long id) {
        return oxoVersionSnapshotConverter.convertEntityToDo(bomsOxoVersionSnapshotDao.getById(id));
    }

    @Override
    public OxoVersionSnapshotAggr queryLastReleaseSnapshotByModel(String modelCode, OxoSnapshotEnum type) {
        return oxoVersionSnapshotConverter.convertEntityToDo(
                bomsOxoVersionSnapshotDao.queryLastReleaseSnapshotByModel(modelCode, type != null ? type.getCode() : null)
        );
    }

    @Override
    public List<OxoVersionSnapshotAggr> queryOxoVersionSnapshotByModelCode(String modelCode, String version, String type) {
        List<OxoVersionSnapshotAggr> snapshotAggrs =
                oxoVersionSnapshotConverter.convertEntityListToDoList(
                        bomsOxoVersionSnapshotDao.queryBomsOxoVersionSnapshotsByModelOrVersionOrType
                                (modelCode, version, type));


        if (CollectionUtils.isNotEmpty(snapshotAggrs)) {

            snapshotAggrs.forEach(snapshot -> {
                snapshot.setOxoSnapshot(snapshot.findOxoSnapshot());
            });
        }

        return snapshotAggrs;
    }

    @Override
    public List<OxoVersionSnapshotAggr> queryBomsOxoVersionSnapshotsByModel(String modelCode) {
        return oxoVersionSnapshotConverter.convertEntityListToDoList(bomsOxoVersionSnapshotDao.
                queryBomsOxoVersionSnapshotsByModelOrVersionOrType(modelCode, null, null));
    }
}
