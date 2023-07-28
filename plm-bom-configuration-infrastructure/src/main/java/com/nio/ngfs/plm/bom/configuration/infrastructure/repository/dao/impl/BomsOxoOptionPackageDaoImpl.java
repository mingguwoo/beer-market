package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoOptionPackageMapper;
import lombok.extern.slf4j.Slf4j;
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
    public void insertOxoOptionPackages(List<OxoPackageInfoAggr> oxoPackages) {
        getBaseMapper().insertOxoOptionPackages(BeanConvertUtils.convertTo(oxoPackages,BomsOxoOptionPackageEntity::new));
    }

    @Override
    public List<BomsOxoOptionPackageEntity> queryOxoListByBaseVehicle(Long baseVehicleId) {
        LambdaQueryWrapper<BomsOxoOptionPackageEntity> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoOptionPackageEntity::getBaseVehicleId,baseVehicleId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }
}
