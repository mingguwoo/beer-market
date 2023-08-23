package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductContextMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductContextDaoImpl extends AbstractDao<BomsProductContextMapper, BomsProductContextEntity, WherePageRequest<BomsProductContextEntity>> implements BomsProductContextDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductContextEntity> bomsProductContextEntityWherePageRequest, LambdaQueryWrapper<BomsProductContextEntity> queryWrapper) {

    }

    @Override
    public List<BomsProductContextEntity> queryByModelCode(String modelCode) {
        LambdaQueryWrapper<BomsProductContextEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductContextEntity::getModelCode, modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductContextEntity> queryByModelAndModelYearList(String modelCode, List<String> modelYearList) {
        if (CollectionUtils.isEmpty(modelYearList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsProductContextEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductContextEntity::getModelCode, modelCode);
        lambdaQueryWrapper.in(BomsProductContextEntity::getModelYear, modelYearList);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
