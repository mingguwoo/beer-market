package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigMapper;
import lombok.extern.slf4j.Slf4j;
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
    public BomsProductConfigEntity getByPcId(String pcId) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getPcId, pcId);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigEntity> queryByModel(String model) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getModel, model);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public BomsProductConfigEntity queryLastPcByModelAndModelYear(String model, String modelYear) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(BomsProductConfigEntity::getPcId, model + " " + modelYear);
        lambdaQueryWrapper.orderByDesc(BomsProductConfigEntity::getPcId);
        lambdaQueryWrapper.last("limit 1");
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public BomsProductConfigEntity getByName(String name) {
        LambdaQueryWrapper<BomsProductConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigEntity::getName, name);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

}
