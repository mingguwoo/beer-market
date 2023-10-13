package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductConfigDaoImpl extends AbstractDao<BomsProductConfigMapper, BomsProductConfigEntity, WherePageRequest<BomsProductConfigEntity>> implements BomsProductConfigDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductConfigEntity> bomsProductConfigEntityWherePageRequest, LambdaQueryWrapper<BomsProductConfigEntity> queryWrapper) {
    }

    @Override
    public void remove(BomsProductConfigEntity entity) {
        getBaseMapper().remove(entity);
    }

    @Override
    public BomsProductConfigEntity getByPcId(String pcId) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getPcId, pcId);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigEntity> queryByModel(String modelCode) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getModelCode, modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public BomsProductConfigEntity queryLastPcByModelAndModelYear(String modelCode, String modelYear) {
        // 此处需包含逻辑删除的数据
        String pcIdPrefix = modelCode + " " + modelYear + "-";
        return getBaseMapper().queryLastPcByPcIdPrefix(pcIdPrefix);
    }

    @Override
    public BomsProductConfigEntity getByName(String name) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getName, name);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigEntity> queryByModelAndModelYearList(String modelCode, List<String> modelYearList) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getModelCode, modelCode);
        if (CollectionUtils.isNotEmpty(modelYearList)) {
            lambdaQueryWrapper.in(BomsProductConfigEntity::getModelYear, modelYearList);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigEntity> queryByPcIdList(List<String> pcIdList, boolean includeDelete) {
        if (CollectionUtils.isEmpty(pcIdList)) {
            return Lists.newArrayList();
        }
        if (includeDelete) {
            // 此处需包含逻辑删除的数据
            return getBaseMapper().queryByPcIdList(pcIdList);
        }
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsProductConfigEntity::getPcId, pcIdList);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

}
