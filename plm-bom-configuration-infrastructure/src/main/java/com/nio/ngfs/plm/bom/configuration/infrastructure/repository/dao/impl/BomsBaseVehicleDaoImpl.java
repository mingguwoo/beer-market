package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsBaseVehicleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getBaseVehicleId, baseVehicleId);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<BomsBaseVehicleEntity> queryByModel(String modelCode, Boolean isMaturity) {
        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        //仅包含Maturity为P且Status为Active的Base Vehicle
        if (isMaturity) {
            lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getMaturity, "P");
        }
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
        if(StringUtils.isNotBlank(modelYear)) {
            lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelYear, modelYear);
        }
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Lists.newArrayList();
        }
        // 此处需包含逻辑删除的数据
        return getBaseMapper().queryByIdList(idList);
    }

    @Override
    public List<BomsBaseVehicleEntity> queryByModelCodeAndIdsAndActive(List<Long> headIds,String modelCode) {


        if (CollectionUtils.isEmpty(headIds)) {
            return Lists.newArrayList();
        }

        LambdaQueryWrapper<BomsBaseVehicleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsBaseVehicleEntity::getId, headIds);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getModelCode,modelCode);
        lambdaQueryWrapper.eq(BomsBaseVehicleEntity::getStatus, StatusEnum.ACTIVE.getStatus());
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsBaseVehicleEntity> bomsBasicVehicleEntityWherePageRequest, LambdaQueryWrapper<BomsBaseVehicleEntity> queryWrapper) {
    }

}
