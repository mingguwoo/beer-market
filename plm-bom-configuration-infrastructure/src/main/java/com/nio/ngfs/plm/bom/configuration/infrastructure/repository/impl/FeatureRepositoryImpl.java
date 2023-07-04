package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureLibraryMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
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
    public FeatureAggr find(String featureCode, FeatureTypeEnum featureType) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getFeatureCode, featureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, featureType.getType());
        return FeatureConverter.convertEntityToDo(getBaseMapper().selectOne(lambdaQueryWrapper));
    }

    @Override
    public FeatureAggr find(Long id) {
        return FeatureConverter.convertEntityToDo(getBaseMapper().selectById(id));
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode) {
        return null;
    }

    @Override
    public List<FeatureAggr> listFeatureLibrary() {
        return null;
    }

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsFeatureLibraryEntity> featureEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureLibraryEntity> queryWrapper) {
    }

}
