package com.sh.beer.market.infrastructure.repository.impl;

import com.sh.beer.market.domain.model.basevehicle.BaseVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/7/18
 */
@Repository
@RequiredArgsConstructor
public class BaseVehicleRepositoryImpl implements BaseVehicleRepository {

    /*private final BomsBaseVehicleDao bomsBaseVehicleDao;
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
    *//**
     * 根据Model
     *//*
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
    public void batchSave(List<BaseVehicleAggr> baseVehicleList) {
        bomsBaseVehicleDao.saveOrUpdateBatch(baseVehicleConverter.convertDoListToEntityList(baseVehicleList));
    }

    @Override
    public List<BaseVehicleAggr> queryByIdList(List<Long> idList) {
        return baseVehicleConverter.convertEntityListToDoList(bomsBaseVehicleDao.listByIds(idList));
    }

    @Override
    public List<BaseVehicleAggr> queryByModel(String modelCode,Boolean isMaturity) {
        return baseVehicleConverter.convertEntityListToDoList(bomsBaseVehicleDao.queryByModel(modelCode,isMaturity));
    }

*/
}
