package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoFeatureOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    public List<BomsOxoFeatureOptionEntity> getBaseVehicleOptions(List<String> featureList, String modelCode) {
        LambdaQueryWrapper<BomsOxoFeatureOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getFeatureCode,featureList);
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getModelCode,modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsOxoFeatureOptionEntity> queryOxoFeatureOptionByModel(String modelCode) {
        LambdaQueryWrapper<BomsOxoFeatureOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getModelCode,modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public void insertOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoRowInfoAggrs) {
        getBaseMapper().insertOxoRows(BeanConvertUtils.convertListTo(oxoRowInfoAggrs,BomsOxoFeatureOptionEntity::new));
    }

    @Override
    public List<BomsOxoFeatureOptionEntity> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList) {
        LambdaQueryWrapper<BomsOxoFeatureOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoFeatureOptionEntity::getModelCode, model);
        if(CollectionUtils.isNotEmpty(featureCodeList)) {
            lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getFeatureCode, featureCodeList);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
