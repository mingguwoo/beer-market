package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsModelYearConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsModelYearConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsModelYearConfigDaoImpl extends AbstractDao<BomsModelYearConfigMapper, BomsModelYearConfigEntity, WherePageRequest<BomsModelYearConfigEntity>> implements BomsModelYearConfigDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsModelYearConfigEntity> bomsModelYearConfigEntityWherePageRequest, LambdaQueryWrapper<BomsModelYearConfigEntity> queryWrapper) {
    }

    @Override
    public BomsModelYearConfigEntity getModelYearConfig(String model, String modelYear) {
        LambdaQueryWrapper<BomsModelYearConfigEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsModelYearConfigEntity::getModel, model);
        lambdaQueryWrapper.eq(BomsModelYearConfigEntity::getModelYear, modelYear);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsModelYearConfigEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

}
