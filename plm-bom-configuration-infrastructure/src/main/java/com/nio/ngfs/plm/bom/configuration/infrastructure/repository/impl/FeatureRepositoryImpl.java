package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
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
import java.util.stream.Collectors;

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
        // 节点变更不会影响父节点，此处不处理父节点
        // 保存子节点列表
        if (CollectionUtils.isNotEmpty(aggr.getChildrenList())) {
            bomsFeatureLibraryDao.updateBatchById(featureConverter.convertDoListToEntityList(
                    aggr.getChildrenList().stream().filter(FeatureAggr::isChildrenChanged).collect(Collectors.toList())
            ));
        }
    }

    @Override
    public FeatureAggr find(FeatureId featureId) {
        return getFeatureAggrWithParentAndChildren(findWithoutParentAndChildren(featureId));
    }

    @Override
    public FeatureAggr getById(Long id) {
        return getFeatureAggrWithParentAndChildren(
                featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getById(id))
        );
    }

    private FeatureAggr findWithoutParentAndChildren(FeatureId featureId) {
        return featureConverter.convertEntityToDo(bomsFeatureLibraryDao.getByFeatureCodeAndType(
                featureId.getFeatureCode(), featureId.getType()
        ));
    }

    /**
     * 构建带父子节点的聚合根
     */
    private FeatureAggr getFeatureAggrWithParentAndChildren(FeatureAggr featureAggr) {
        if (featureAggr == null) {
            return null;
        }
        FeatureTypeEnum typeEnum = FeatureTypeEnum.getByType(featureAggr.getFeatureId().getType());
        // 查询父节点
        if (StringUtils.isNotBlank(typeEnum.getParentType())) {
            featureAggr.setParent(findWithoutParentAndChildren(new FeatureId(featureAggr.getParentFeatureCode(), typeEnum.getParentType())));
        }
        // 查询子节点列表
        if (StringUtils.isNotBlank(typeEnum.getChildrenType())) {
            featureAggr.setChildrenList(queryByParentFeatureCodeAndType(featureAggr.getFeatureId().getFeatureCode(), typeEnum.getChildrenType()));
        }
        FeatureAggrThreadLocal.add(featureAggr);
        return featureAggr;
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCodeAndType(String parentFeatureCode, String type) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByParentFeatureCodeAndType(parentFeatureCode, type));
    }

    @Override
    public List<FeatureAggr> queryByParentFeatureCodeListAndType(List<String> parentFeatureCodeList, String type) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(parentFeatureCodeList, type));
    }

    @Override
    public void batchUpdateStatus(List<Long> idList, String status, String updateUser) {
        bomsFeatureLibraryDao.batchUpdateStatus(idList, status, updateUser);
    }

    @Override
    public List<FeatureAggr> queryByFeatureCode(String featureCode) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByFeatureCode(featureCode));
    }

    @Override
    public List<FeatureAggr> queryByDisplayNameCatalogAndType(String displayName, String catalog, String type) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByDisplayNameCatalogAndType(displayName, catalog, type));
    }

    @Override
    public List<FeatureAggr> queryByFeatureOptionCodeList(List<String> featureOptionCodeList) {
        return featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByFeatureOptionCodeList(featureOptionCodeList));
    }

}
