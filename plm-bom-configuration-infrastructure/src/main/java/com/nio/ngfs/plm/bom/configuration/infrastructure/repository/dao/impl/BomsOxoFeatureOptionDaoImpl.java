package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoFeatureOptionMapper;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.GetBaseVehicleOptionsQry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@Slf4j
public class BomsOxoFeatureOptionDaoImpl extends AbstractDao<BomsOxoFeatureOptionMapper, BomsOxoFeatureOptionEntity, WherePageRequest<BomsOxoFeatureOptionEntity>> implements BomsOxoFeatureOptionDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoFeatureOptionEntity> bomsOxoFeatureOptionEntityWherePageRequest, LambdaQueryWrapper<BomsOxoFeatureOptionEntity> queryWrapper) {
    }

    @Override
    public List<BomsOxoFeatureOptionEntity> getBaseVehicleOptions(List<String> featureList, GetBaseVehicleOptionsQry qry) {
        LambdaQueryWrapper<BomsOxoFeatureOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getFeatureCode,featureList);
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getModelCode,qry.getModelCode());
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }
}
