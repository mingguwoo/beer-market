package com.sh.beer.market.infrastructure.repository.dao.impl;


import com.sh.beer.market.domain.model.WherePageRequest;
import com.sh.beer.market.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.sh.beer.market.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.sh.beer.market.infrastructure.repository.mapper.BomsBaseVehicleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/7/18
 */
@Repository
@Slf4j
public class BomsBaseVehicleDaoImpl extends AbstractDao<BomsBaseVehicleMapper, BomsBaseVehicleEntity, WherePageRequest<BomsBaseVehicleEntity>> implements BomsBaseVehicleDao {
    /*@Override
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
    }*/

}
