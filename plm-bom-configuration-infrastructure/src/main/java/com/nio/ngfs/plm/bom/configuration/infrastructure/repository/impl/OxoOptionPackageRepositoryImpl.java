package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoOptionPackageConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoOptionPackageRepositoryImpl implements OxoOptionPackageRepository {

    private final BomsOxoOptionPackageDao bomsOxoOptionPackageDao;
    private final OxoOptionPackageConverter oxoOptionPackageConverter;

    @Override
    public void save(OxoOptionPackageAggr aggr) {
        DaoSupport.saveOrUpdate(bomsOxoOptionPackageDao, oxoOptionPackageConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoOptionPackageAggr find(Long id) {
        return oxoOptionPackageConverter.convertEntityToDo(bomsOxoOptionPackageDao.getById(id));
    }

    @Override
    public void batchSave(List<OxoOptionPackageAggr> aggrList) {
        DaoSupport.batchSaveOrUpdate(bomsOxoOptionPackageDao, oxoOptionPackageConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public void insertOxoOptionPackages(List<OxoOptionPackageAggr> oxoPackages) {
        if (CollectionUtils.isNotEmpty(oxoPackages)) {
            bomsOxoOptionPackageDao.insertOxoOptionPackages(oxoPackages);
        }
    }

    @Override
    public List<OxoOptionPackageAggr> queryByBaseVehicleId(Long baseVehicleId) {
        return oxoOptionPackageConverter.convertEntityListToDoList(bomsOxoOptionPackageDao.queryOxoListByBaseVehicle(baseVehicleId));
    }

    @Override
    public List<OxoOptionPackageAggr> queryByFeatureOptionIdsAndHeadIdsList(List<Long> featureOptionIdList, List<Long> headIds) {
        return oxoOptionPackageConverter.convertEntityListToDoList(bomsOxoOptionPackageDao.queryByFeatureOptionIdList(featureOptionIdList, headIds));
    }

    @Override
    public void inserOxoOptionPackagesByOxoOptionPackages(List<OxoOptionPackageAggr> oxoOptionPackageAggrs) {
        bomsOxoOptionPackageDao.saveBatch(oxoOptionPackageConverter.convertDoListToEntityList(oxoOptionPackageAggrs));
    }

    @Override
    public List<OxoOptionPackageAggr> queryByBaseVehicleIds(List<Long> rowIds) {
        List<OxoOptionPackageAggr> oxoOptionPackageAggrs =
                oxoOptionPackageConverter.convertEntityListToDoList(bomsOxoOptionPackageDao.queryOxoOptionPackageByRowIds(rowIds));

        if (CollectionUtils.isNotEmpty(oxoOptionPackageAggrs)) {
            oxoOptionPackageAggrs.forEach(x -> {
                if (StringUtils.isBlank(x.getDescription())) {
                    x.setDescription(StringUtils.EMPTY);
                }
            });
        }
        return oxoOptionPackageAggrs;
    }

    @Override
    public void saveOrUpdatebatch(List<OxoOptionPackageAggr> oxoOptionPackageAggrs) {
        bomsOxoOptionPackageDao.saveOrUpdateBatch(oxoOptionPackageConverter.convertDoListToEntityList(oxoOptionPackageAggrs));
    }

    @Override
    public void removeByBaseVehicleIds(Long baseVehicleId) {
        bomsOxoOptionPackageDao.removeByBaseVehicleIds(baseVehicleId);
    }

}
