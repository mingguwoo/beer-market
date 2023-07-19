package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.BaseVehicleConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Repository
@RequiredArgsConstructor
public class BaseVehicleRepositoryImpl implements BaseVehicleRepository {

    private final BomsBasicVehicleDao bomsBasicVehicleDao;
    private final BaseVehicleConverter baseVehicleConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BaseVehicleAggr baseVehicleAggr) {
        DaoSupport.saveOrUpdate(bomsBasicVehicleDao,baseVehicleConverter.convertDoToEntity(baseVehicleAggr));
    }

    @Override
    public BaseVehicleAggr find(String s) {
        return null;
    }

    @Override
    public List<BaseVehicleAggr> queryByModelModelYearRegionDriveHandSalesVersion(String model, String modelYear, String region, String driveHand, String salesVersion) {
        return baseVehicleConverter.convertEntityListToDoList(bomsBasicVehicleDao.queryByModelModelYearRegionDriveHandSalesVersion(model, modelYear, region, driveHand, salesVersion));
    }
}
