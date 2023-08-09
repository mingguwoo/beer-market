package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig.ModelYearConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig.ModelYearConfigId;
import com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig.ModelYearConfigRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.ModelYearConfigConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsModelYearConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Repository
@RequiredArgsConstructor
public class ModelYearConfigRepositoryImpl implements ModelYearConfigRepository {

    private final BomsModelYearConfigDao bomsModelYearConfigDao;
    private final ModelYearConfigConverter modelYearConfigConverter;

    @Override
    public void save(ModelYearConfigAggr aggr) {
        DaoSupport.saveOrUpdate(bomsModelYearConfigDao, modelYearConfigConverter.convertDoToEntity(aggr));
    }

    @Override
    public ModelYearConfigAggr find(ModelYearConfigId modelYearConfigId) {
        return modelYearConfigConverter.convertEntityToDo(bomsModelYearConfigDao.getModelYearConfig(
                modelYearConfigId.getModel(), modelYearConfigId.getModelYear()
        ));
    }

}
