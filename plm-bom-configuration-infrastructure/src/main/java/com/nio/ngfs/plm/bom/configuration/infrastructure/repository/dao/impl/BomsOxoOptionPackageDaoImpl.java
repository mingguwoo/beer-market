package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoOptionPackageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@Slf4j
public class BomsOxoOptionPackageDaoImpl extends AbstractDao<BomsOxoOptionPackageMapper, BomsOxoOptionPackageEntity, WherePageRequest<BomsOxoOptionPackageEntity>> implements BomsOxoOptionPackageDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoOptionPackageEntity> bomsOxoOptionPackageEntityWherePageRequest, LambdaQueryWrapper<BomsOxoOptionPackageEntity> queryWrapper) {
    }

    @Override
    public List<BomsOxoOptionPackageEntity> queryOxoOptionPackageByRowIds(List<Long> rowIds) {

        LambdaQueryWrapper<BomsOxoOptionPackageEntity> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsOxoOptionPackageEntity::getFeatureOptionId,rowIds);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public void insertOxoOptionPackages(List<BomsOxoOptionPackageEntity> oxoPackages) {
        getBaseMapper().insertOxoOptionPackages(oxoPackages);
    }

    @Override
    public List<BomsOxoOptionPackageEntity> queryByFeatureOptionIdList(List<Long> featureOptionIdList,List<Long> headIds) {
        if (CollectionUtils.isEmpty(featureOptionIdList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsOxoOptionPackageEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(CollectionUtils.isNotEmpty(featureOptionIdList)) {
            lambdaQueryWrapper.in(BomsOxoOptionPackageEntity::getFeatureOptionId, featureOptionIdList);
        }

        if(CollectionUtils.isNotEmpty(headIds)) {
            lambdaQueryWrapper.in(BomsOxoOptionPackageEntity::getBaseVehicleId, headIds);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsOxoOptionPackageEntity> queryOxoListByBaseVehicle(Long baseVehicleId) {
        LambdaQueryWrapper<BomsOxoOptionPackageEntity> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoOptionPackageEntity::getBaseVehicleId,baseVehicleId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public void removeByBaseVehicleIds(Long baseVehicleId) {
        LambdaQueryWrapper<BomsOxoOptionPackageEntity> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoOptionPackageEntity::getBaseVehicleId, baseVehicleId);
        getBaseMapper().delete(lambdaQueryWrapper);
    }
}
