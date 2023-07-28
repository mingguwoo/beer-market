package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoOptionPackageConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
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
    public void insertOxoOptionPackages(List<OxoPackageInfoAggr> oxoPackages) {
        bomsOxoOptionPackageDao.insertOxoOptionPackages(oxoPackages);
    }

    @Override
    public List<OxoOptionPackageAggr> queryByBaseVehicleId(Long baseVehicleId) {
        return oxoOptionPackageConverter.convertEntityListToDoList(bomsOxoOptionPackageDao.queryOxoListByBaseVehicle(baseVehicleId));
    }

    @Override
    public void inserOxoOptionPackagesByModelCodeAndOxoOptionPackages(List<OxoOptionPackageAggr> oxoOptionPackageAggrs) {
        bomsOxoOptionPackageDao.saveBatch(oxoOptionPackageConverter.convertDoListToEntityList(oxoOptionPackageAggrs));
    }
}