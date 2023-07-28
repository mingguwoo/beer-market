package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoFeatureOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@RequiredArgsConstructor
public class OxoFeatureOptionRepositoryImpl implements OxoFeatureOptionRepository {

    private final BomsOxoFeatureOptionDao bomsOxoFeatureOptionDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final OxoFeatureOptionConverter oxoFeatureOptionConverter;

    @Override
    public void save(OxoFeatureOptionAggr aggr) {
        DaoSupport.saveOrUpdate(bomsOxoFeatureOptionDao, oxoFeatureOptionConverter.convertDoToEntity(aggr));
    }

    @Override
    public OxoFeatureOptionAggr find(Long id) {
        return oxoFeatureOptionConverter.convertEntityToDo(bomsOxoFeatureOptionDao.getById(id));
    }

    @Override
    public List<OxoFeatureOptionAggr> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList) {
        return oxoFeatureOptionConverter.convertEntityListToDoList(bomsOxoFeatureOptionDao.queryByModelAndFeatureCodeList(model, featureCodeList));
    }

    @Override
    public void batchSave(List<OxoFeatureOptionAggr> aggrList) {
        bomsOxoFeatureOptionDao.updateBatchById(oxoFeatureOptionConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public List<OxoFeatureOptionAggr> queryFeaturesByModel(String model) {
        List<BomsOxoFeatureOptionEntity> featureOptionEntities=
                bomsOxoFeatureOptionDao.queryByModelAndFeatureCodeList(model,Lists.newArrayList());


        if(CollectionUtils.isNotEmpty(featureOptionEntities)){

        }

        //bomsFeatureLibraryDao.queryByFeatureCode()
        return null;
    }


    @Override
    public List<OxoFeatureOptionAggr> queryFeatureListsByModel(String modelCode) {

        List<BomsOxoFeatureOptionEntity> bomsOxoFeatureOptionEntities =
                bomsOxoFeatureOptionDao.queryOxoFeatureOptionByModel(modelCode);

        if (CollectionUtils.isEmpty(bomsOxoFeatureOptionEntities)) {
            return Lists.newArrayList();
        }

        List<String> featureCodes = bomsOxoFeatureOptionEntities.stream().map(BomsOxoFeatureOptionEntity::getFeatureCode).distinct().toList();


        List<BomsFeatureLibraryEntity> libraryEntities = bomsFeatureLibraryDao.queryByFeatureCodes(featureCodes);


        return bomsOxoFeatureOptionEntities.stream().map(x -> {

            OxoFeatureOptionAggr oxoFeatureOptionAggr = new OxoFeatureOptionAggr();
            oxoFeatureOptionAggr.setFeatureCode(x.getFeatureCode());
            oxoFeatureOptionAggr.setId(x.getId());
            BomsFeatureLibraryEntity entity =
                    libraryEntities.stream().filter(y -> StringUtils.equals(x.getFeatureCode(), y.getFeatureCode())).findFirst().orElse(null);

            if (Objects.nonNull(entity)) {

                oxoFeatureOptionAggr.setCatalog(entity.getCatalog());
                oxoFeatureOptionAggr.setParentFeatureCode(entity.getParentFeatureCode());
                oxoFeatureOptionAggr.setChineseName(entity.getChineseName());
                oxoFeatureOptionAggr.setDisplayName(entity.getDisplayName());
                oxoFeatureOptionAggr.setType(entity.getType());

            }
            oxoFeatureOptionAggr.setRuleCheck(x.getRuleCheck());
            oxoFeatureOptionAggr.setComment(x.getComment());
            oxoFeatureOptionAggr.setSortDelete(Integer.valueOf(x.getSoftDelete()));
            oxoFeatureOptionAggr.setModelCode(modelCode);
            oxoFeatureOptionAggr.setSort(x.getSort());

            return oxoFeatureOptionAggr;

        }).toList();
    }
}
