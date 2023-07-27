package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsBasicVehicleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Repository
@Slf4j
public class BomsBasicVehicleDaoImpl extends AbstractDao<BomsBasicVehicleMapper, BomsBasicVehicleEntity, WherePageRequest<BomsBasicVehicleEntity>> implements BomsBasicVehicleDao{
    @Override
    public List<BomsBasicVehicleEntity> queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(String model, String modelYear, String regionOptionCode, String driveHand, String salesVersion) {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModelCode, model);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModelYear, modelYear);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getRegionOptionCode, regionOptionCode);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getDriveHand, driveHand);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getSalesVersion, salesVersion);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public BomsBasicVehicleEntity getLastestBaseVehicle() {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(BomsBasicVehicleEntity::getId).last("limit 1");
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public BomsBasicVehicleEntity queryBaseVehicleByBaseVehicleId(String baseVehicleId) {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getBaseVehicleId,baseVehicleId);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBasicVehicleEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<BomsBasicVehicleEntity> queryByModel(String modelCode) {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        lambdaQueryWrapper.orderByDesc(BomsBasicVehicleEntity::getModelYear);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBasicVehicleEntity> queryCopyFromModel(String modelCode) {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }


    @Override
    protected void fuzzyConditions(WherePageRequest<BomsBasicVehicleEntity> bomsBasicVehicleEntityWherePageRequest, LambdaQueryWrapper<BomsBasicVehicleEntity> queryWrapper) {
    }

}
