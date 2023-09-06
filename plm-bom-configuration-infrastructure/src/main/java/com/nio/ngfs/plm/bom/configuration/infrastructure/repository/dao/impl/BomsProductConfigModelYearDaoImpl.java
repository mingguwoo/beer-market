package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelYearDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelYearEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigModelYearMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductConfigModelYearDaoImpl extends AbstractDao<BomsProductConfigModelYearMapper, BomsProductConfigModelYearEntity, WherePageRequest<BomsProductConfigModelYearEntity>> implements BomsProductConfigModelYearDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductConfigModelYearEntity> bomsModelYearConfigEntityWherePageRequest, LambdaQueryWrapper<BomsProductConfigModelYearEntity> queryWrapper) {
    }

    @Override
    public BomsProductConfigModelYearEntity getModelYearConfig(String model, String modelYear) {
        LambdaQueryWrapper<BomsProductConfigModelYearEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigModelYearEntity::getModel, model);
        lambdaQueryWrapper.eq(BomsProductConfigModelYearEntity::getModelYear, modelYear);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigModelYearEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<BomsProductConfigModelYearEntity> queryByModel(String model) {
        LambdaQueryWrapper<BomsProductConfigModelYearEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigModelYearEntity::getModel, model);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
