package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureChangeLogConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Repository
@RequiredArgsConstructor
public class FeatureChangeLogRepositoryImpl implements FeatureChangeLogRepository {

    private final BomsFeatureChangeLogDao bomsFeatureChangeLogDao;
    private final FeatureChangeLogConverter featureChangeLogConverter;

    @Override
    public void save(FeatureChangeLogAggr aggr) {
        DaoSupport.saveOrUpdate(bomsFeatureChangeLogDao, featureChangeLogConverter.convertDoToEntity(aggr));
    }

    @Override
    public FeatureChangeLogAggr find(Long id) {
        return featureChangeLogConverter.convertEntityToDo(bomsFeatureChangeLogDao.getById(id));
    }

    @Override
    public void batchSave(List<FeatureChangeLogAggr> changeLogAggrList) {
        if (CollectionUtils.isEmpty(changeLogAggrList)) {
            return;
        }
        bomsFeatureChangeLogDao.saveBatch(featureChangeLogConverter.convertDoListToEntityList(changeLogAggrList));
    }

}
