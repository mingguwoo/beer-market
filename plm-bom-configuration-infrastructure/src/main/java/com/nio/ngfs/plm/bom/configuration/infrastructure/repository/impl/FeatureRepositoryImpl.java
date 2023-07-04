package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureLibraryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
public class FeatureRepositoryImpl extends AbstractRepository<BomsFeatureLibraryMapper, BomsFeatureLibraryEntity, WherePageRequest<BomsFeatureLibraryEntity>> implements FeatureRepository {

    @Override
    public void save(FeatureAggr aggr) {
        BomsFeatureLibraryEntity entity = FeatureConverter.convertDoToEntity(aggr);
        if (entity.getId() != null) {
            getBaseMapper().updateById(entity);
        } else {
            getBaseMapper().insert(entity);
        }
    }

    @Override
    public FeatureAggr find(FeatureId featureId) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getFeatureCode, featureId.getFeatureCode());
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, featureId.getType());
        return FeatureConverter.convertEntityToDo(getBaseMapper().selectOne(lambdaQueryWrapper));
    }


    @Override
    public FeatureAggr getById(Long id) {
        return FeatureConverter.convertEntityToDo(getBaseMapper().selectById(id));
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getParentFeatureCode, parentFeatureCode);
        return FeatureConverter.convertEntityListToDoList(getBaseMapper().selectList(lambdaQueryWrapper));
    }

    @Override
    public List<FeatureAggr> queryAll() {
        return FeatureConverter.convertEntityListToDoList(getBaseMapper().selectList(new LambdaQueryWrapper<>()));
    }

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsFeatureLibraryEntity> featureEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureLibraryEntity> queryWrapper) {
    }

}
