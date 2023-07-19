package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
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
    public List<BomsBasicVehicleEntity> queryByModelModelYearRegionDriveHandSalesVersion(String model, String modelYear, String region, String driveHand, String salesVersion) {
        LambdaQueryWrapper<BomsBasicVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModel, model);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getModelYear, modelYear);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getRegion, region);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getDriveHand, driveHand);
        lambdaQueryWrapper.eq(BomsBasicVehicleEntity::getSalesVersion, salesVersion);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsBasicVehicleEntity> bomsBasicVehicleEntityWherePageRequest, LambdaQueryWrapper<BomsBasicVehicleEntity> queryWrapper) {
    }

}
