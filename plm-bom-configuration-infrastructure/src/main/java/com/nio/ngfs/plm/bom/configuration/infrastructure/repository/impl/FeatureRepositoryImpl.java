package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl.AbstractRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.po.BomsFeatureLibraryPO;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureLibraryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
public class FeatureRepositoryImpl extends AbstractRepository<BomsFeatureLibraryMapper, BomsFeatureLibraryPO, WherePageRequest<BomsFeatureLibraryPO>> implements FeatureRepository {

    @Override
    public void save(FeatureAggr featureAggr) {
        BomsFeatureLibraryPO featureLibraryPo = FeatureConverter.convertEntityToPo(featureAggr);
        if (featureLibraryPo.getId() != null) {
            getBaseMapper().updateById(featureLibraryPo);
        } else {
            getBaseMapper().insert(featureLibraryPo);
        }
    }

    @Override
    public FeatureAggr find(String featureCode, FeatureTypeEnum featureType) {
        LambdaQueryWrapper<BomsFeatureLibraryPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryPO::getFeatureCode, featureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryPO::getType, featureType.getType());
        return FeatureConverter.convertPoToEntity(getBaseMapper().selectOne(lambdaQueryWrapper));
    }

    @Override
    public FeatureAggr find(Long id) {
        return FeatureConverter.convertPoToEntity(getBaseMapper().selectById(id));
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
    protected void fuzzyConditions(WherePageRequest<BomsFeatureLibraryPO> featureEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureLibraryPO> queryWrapper) {
    }

}
