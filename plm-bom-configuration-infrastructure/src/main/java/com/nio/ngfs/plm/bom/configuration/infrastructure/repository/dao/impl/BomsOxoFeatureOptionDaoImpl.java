package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoFeatureOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    public List<BomsOxoFeatureOptionEntity> queryOxoFeatureOptionByModel(String modelCode,Boolean sortDelete) {
        LambdaQueryWrapper<BomsOxoFeatureOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsOxoFeatureOptionEntity::getModelCode,modelCode);
        if(sortDelete){
            lambdaQueryWrapper.eq(BomsOxoFeatureOptionEntity::getSoftDelete,0);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }


    @Override
    public void updateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoRowInfoAggrs) {
        oxoRowInfoAggrs.forEach(oxoFeatureOptionAggr -> {
            BomsOxoFeatureOptionEntity entity=new BomsOxoFeatureOptionEntity();
            if(StringUtils.isNotBlank(oxoFeatureOptionAggr.getComment())) {
                entity.setComment(oxoFeatureOptionAggr.getComment());
            }

            if(StringUtils.isNotBlank(oxoFeatureOptionAggr.getRuleCheck())) {
                entity.setRuleCheck(oxoFeatureOptionAggr.getRuleCheck());
            }
            entity.setId(oxoFeatureOptionAggr.getId());
            entity.setUpdateUser(oxoFeatureOptionAggr.getUpdateUser());
            //更新 oxo row
            getBaseMapper().updateById(entity);
        });
    }

    @Override
    public void insertOrUpdateBomsOxoFeature(List<BomsOxoFeatureOptionEntity> bomsOxoFeatureOptionEntities) {
        getBaseMapper().insertOxoRows(bomsOxoFeatureOptionEntities);
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
