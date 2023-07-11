package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Repository
@RequiredArgsConstructor
public class FeatureRepositoryImpl implements FeatureRepository {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final FeatureConverter featureConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(FeatureAggr aggr) {
        DaoSupport.saveOrUpdate(bomsFeatureLibraryDao, featureConverter.convertDoToEntity(aggr));
        // 保存子节点列表
        if (CollectionUtils.isNotEmpty(aggr.getChildrenList())) {
            bomsFeatureLibraryDao.updateBatchById(featureConverter.convertDoListToEntityList(aggr.getChildrenList()));
        }
    }

    @Override
    public FeatureAggr find(FeatureId featureId) {
        return getFeatureAggrWithChildren(
                featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getByFeatureCodeAndType(
                        featureId.getFeatureCode(), featureId.getType()
                ))
        );
    }

    @Override
    public FeatureAggr getById(Long id) {
        return getFeatureAggrWithChildren(
                featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getById(id))
        );
    }

    private FeatureAggr getFeatureAggrWithChildren(FeatureAggr featureAggr) {
        if (featureAggr == null) {
            return null;
        }
        // 查询子节点列表
        FeatureTypeEnum typeEnum = FeatureTypeEnum.getByType(featureAggr.getFeatureId().getType());
        if (StringUtils.isNotBlank(typeEnum.getChildrenType())) {
            featureAggr.setChildrenList(queryByParentFeatureCodeAndType(featureAggr.getFeatureId().getFeatureCode(), typeEnum.getChildrenType()));
        }
        return featureAggr;
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCodeAndType(String parentFeatureCode, String type) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByParentFeatureCodeAndType(parentFeatureCode, type));
    }

    @Override
    public List<FeatureAggr> queryAll() {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryAll());
    }

}
