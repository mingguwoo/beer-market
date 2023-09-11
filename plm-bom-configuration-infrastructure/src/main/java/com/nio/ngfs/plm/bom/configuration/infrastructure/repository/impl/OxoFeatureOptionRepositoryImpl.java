package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.OxoFeatureOptionConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

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
        DaoSupport.batchSaveOrUpdate(bomsOxoFeatureOptionDao, oxoFeatureOptionConverter.convertDoListToEntityList(aggrList));
    }

    @Override
    public void batchRemove(List<OxoFeatureOptionAggr> aggrList) {
        if (CollectionUtils.isEmpty(aggrList)) {
            return;
        }
        bomsOxoFeatureOptionDao.removeBatchByIds(aggrList);
    }

    @Override
    public List<OxoFeatureOptionAggr> queryFeatureListsByModel(String modelCode, List<String> roleNames) {

        List<BomsOxoFeatureOptionEntity> bomsOxoFeatureOptionEntities =
                bomsOxoFeatureOptionDao.queryOxoFeatureOptionByModel(modelCode, null);

        if (CollectionUtils.isEmpty(bomsOxoFeatureOptionEntities)) {
            return Lists.newArrayList();
        }

        List<String> featureCodes = bomsOxoFeatureOptionEntities.stream().map(BomsOxoFeatureOptionEntity::getFeatureCode).distinct().toList();


        List<BomsFeatureLibraryEntity> libraryEntities = bomsFeatureLibraryDao.queryByFeatureCodes(featureCodes);

        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs = Lists.newArrayList();

        boolean result = CollectionUtils.isNotEmpty(roleNames) && !roleNames.contains(ConfigConstants.CONFIG_ADMIN);
        bomsOxoFeatureOptionEntities.forEach(x -> {
            BomsFeatureLibraryEntity entity =
                    libraryEntities.stream().filter(
                            y -> StringUtils.equals(x.getFeatureCode(), y.getFeatureCode())).findFirst().orElse(null);

            if (Objects.isNull(entity)) {
                return;
            }

            if (result && !StringUtils.equals(entity.getCatalog(), FeatureCatalogEnum.ENGINEERING.getCatalog())) {
                return;
            }
            OxoFeatureOptionAggr oxoFeatureOptionAggr = new OxoFeatureOptionAggr();
            oxoFeatureOptionAggr.setFeatureCode(x.getFeatureCode());
            oxoFeatureOptionAggr.setId(x.getId());


            oxoFeatureOptionAggr.setCatalog(entity.getCatalog());
            oxoFeatureOptionAggr.setParentFeatureCode(entity.getParentFeatureCode());
            oxoFeatureOptionAggr.setChineseName(entity.getChineseName());
            oxoFeatureOptionAggr.setDisplayName(entity.getDisplayName());
            oxoFeatureOptionAggr.setType(entity.getType());

            oxoFeatureOptionAggr.setRuleCheck(x.getRuleCheck());
            oxoFeatureOptionAggr.setComment(x.getComment());
            oxoFeatureOptionAggr.setSoftDelete(x.getSoftDelete());
            oxoFeatureOptionAggr.setModelCode(modelCode);
            oxoFeatureOptionAggr.setSort(x.getSort());

            oxoFeatureOptionAggrs.add(oxoFeatureOptionAggr);

        });

        return oxoFeatureOptionAggrs;
    }

    @Override
    public List<OxoFeatureOptionAggr> queryFeatureListsByModelAndSortDelete(String modelCode, Boolean isSoftDelete) {
        List<BomsOxoFeatureOptionEntity> entities =
                bomsOxoFeatureOptionDao.queryOxoFeatureOptionByModel(modelCode, isSoftDelete);
        return oxoFeatureOptionConverter.convertEntityListToDoList(entities);
    }

    @Override
    public void insertOrUpdateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs) {
        bomsOxoFeatureOptionDao.insertOrUpdateBomsOxoFeature(
                oxoFeatureOptionConverter.convertDoListToEntityList(oxoFeatureOptionAggrs));
    }

    @Override
    public void updateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs) {
        bomsOxoFeatureOptionDao.updateOxoFeatureOptions(oxoFeatureOptionAggrs.stream().map(oxoFeatureOptionAggr -> {
            BomsOxoFeatureOptionEntity entity = new BomsOxoFeatureOptionEntity();
            if (StringUtils.isNotBlank(oxoFeatureOptionAggr.getComment())) {
                entity.setComment(oxoFeatureOptionAggr.getComment());
            }
            if (StringUtils.isNotBlank(oxoFeatureOptionAggr.getRuleCheck())) {
                entity.setRuleCheck(oxoFeatureOptionAggr.getRuleCheck());
            }
            entity.setId(oxoFeatureOptionAggr.getId());
            entity.setUpdateUser(oxoFeatureOptionAggr.getUpdateUser());
            return entity;
        }).toList());
    }

    @Override
    public void restoreOxoFeatureOptionByIds(List<Long> ids, Integer isDelete) {
        bomsOxoFeatureOptionDao.restoreOxoFeatureOptionByIds(ids, isDelete);
    }


}
