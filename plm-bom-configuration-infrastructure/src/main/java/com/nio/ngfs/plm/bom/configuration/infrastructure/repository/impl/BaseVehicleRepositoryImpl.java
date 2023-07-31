package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.generator.BaseVehicleIdGenerator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.BaseVehicleConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
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

    private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleConverter baseVehicleConverter;
    private final BaseVehicleIdGenerator baseVehicleIdGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BaseVehicleAggr baseVehicleAggr) {
        //判断是add还是edit，如果是add，需要加流水号
        if (baseVehicleAggr.getId() == null){
            baseVehicleAggr.setBaseVehicleId(baseVehicleIdGenerator.createBaseVehicleId(RedisKeyConstant.BASE_VEHICLE_ID_KEY));
        }
        BomsBaseVehicleEntity bomsBaseVehicleEntity = baseVehicleConverter.convertDoToEntity(baseVehicleAggr);
        DaoSupport.saveOrUpdate(bomsBaseVehicleDao, bomsBaseVehicleEntity);
        baseVehicleAggr.setId(bomsBaseVehicleEntity.getId());
    }

    @Override
    public BaseVehicleAggr find(String s) {
        return null;
    }
    /**
     * 根据Model
     */
    @Override
    public List<BaseVehicleAggr> queryByModelCodeModelYearRegionDriveHandSalesVersion(String modelCode, String modelYear, String regionOptionCode, String driveHand, String salesVersion) {
        return baseVehicleConverter.convertEntityListToDoList(bomsBaseVehicleDao.queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(modelCode, modelYear, regionOptionCode, driveHand, salesVersion));
    }

    @Override
    public BaseVehicleAggr queryBaseVehicleByBaseVehicleId(String baseVehicleId) {
        return baseVehicleConverter.convertEntityToDo(bomsBaseVehicleDao.queryBaseVehicleByBaseVehicleId(baseVehicleId));
    }

    @Override
    public void removeById(Long id) {
        bomsBaseVehicleDao.removeById(id);
    }


    @Override
    public List<BaseVehicleAggr> queryByModel(String modelCode) {
        return baseVehicleConverter.convertEntityListToDoList(bomsBaseVehicleDao.queryByModel(modelCode));
    }


}
