package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureLibraryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
@RequiredArgsConstructor
public class FeatureRepositoryImpl extends AbstractRepository<BomsFeatureLibraryMapper, BomsFeatureLibraryEntity, WherePageRequest<BomsFeatureLibraryEntity>> implements FeatureRepository {

    private final FeatureConverter featureConverter;

    @Override
    public void save(FeatureAggr aggr) {
        BomsFeatureLibraryEntity entity = featureConverter.convertDoToEntity(aggr);
        if (entity.getId() != null) {
            getBaseMapper().updateById(entity);
        } else {
            getBaseMapper().insert(entity);
        }
    }

    @Override
    public FeatureAggr find(Long id) {
        return featureConverter.convertEntityToDo(getBaseMapper().selectById(id));
    }

    @Override
    public FeatureAggr getByFeatureCodeAndType(String featureCode, String type) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getFeatureCode, featureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, type);
        return featureConverter.convertEntityToDo(getBaseMapper().selectOne(lambdaQueryWrapper));
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getParentFeatureCode, parentFeatureCode);
        return featureConverter.convertEntityListToDoList(getBaseMapper().selectList(lambdaQueryWrapper));
    }

    @Override
    public List<FeatureAggr> queryAll() {
        return featureConverter.convertEntityListToDoList(getBaseMapper().selectList(new LambdaQueryWrapper<>()));
    }

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsFeatureLibraryEntity> featureEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureLibraryEntity> queryWrapper) {
    }

}
