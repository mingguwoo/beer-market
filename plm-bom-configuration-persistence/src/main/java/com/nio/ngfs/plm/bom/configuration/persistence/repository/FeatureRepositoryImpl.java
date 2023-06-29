package com.nio.ngfs.plm.bom.configuration.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.persistence.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.persistence.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.persistence.mapper.BomsFeatureLibraryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
public class FeatureRepositoryImpl extends AbstractRepository<BomsFeatureLibraryMapper, BomsFeatureLibraryEntity, WherePageRequest<BomsFeatureLibraryEntity>> implements FeatureRepository {

    @Override
    public void save(FeatureAggr featureAggr) {
        BomsFeatureLibraryEntity featureEntity = FeatureConverter.convertToEntity(featureAggr);
        if (featureEntity.getId() != null) {
            getBaseMapper().updateById(featureEntity);
        } else {
            getBaseMapper().insert(featureEntity);
        }
    }

    @Override
    public FeatureAggr find(String featureCode, FeatureTypeEnum featureType) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getFeatureCode, featureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, featureType.getType());
        return FeatureConverter.convertEntityTo(getBaseMapper().selectOne(lambdaQueryWrapper));
    }

    @Override
    public FeatureAggr find(Long id) {
        return FeatureConverter.convertEntityTo(getBaseMapper().selectById(id));
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
