package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsBaseVehicleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Repository
@Slf4j
public class BomsBaseVehicleDaoImpl extends AbstractDao<BomsBaseVehicleMapper, BomsBaseVehicleEntity, WherePageRequest<BomsBaseVehicleEntity>> implements BomsBaseVehicleDao {
    @Override
    public List<BomsBaseVehicleEntity> queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(String model, String modelYear, String regionOptionCode, String driveHand, String salesVersion) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode, model);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelYear, modelYear);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getRegionOptionCode, regionOptionCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getDriveHand, driveHand);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getSalesVersion, salesVersion);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public Long getLastestBaseVehicle() {
        return getBaseMapper().getLatestBaseVehicle();
    }

    @Override
    public BomsBaseVehicleEntity queryBaseVehicleByBaseVehicleId(String baseVehicleId) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getBaseVehicleId,baseVehicleId);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<BomsBaseVehicleEntity> queryByModel(String modelCode) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        lambdaQueryWrapper.orderByDesc(BomsBaseVehicleEntity::getModelYear);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryCopyFromModel(String modelCode) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryByModelCodeAndModelYear(String modelCode, String modelYear) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelYear, modelYear);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }


    @Override
    protected void fuzzyConditions(WherePageRequest<BomsBaseVehicleEntity> bomsBasicVehicleEntityWherePageRequest, LambdaQueryWrapper<BomsBaseVehicleEntity> queryWrapper) {
    }

}
