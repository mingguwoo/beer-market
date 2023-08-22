package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigModelOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductConfigModelOptionDaoImpl extends AbstractDao<BomsProductConfigModelOptionMapper, BomsProductConfigModelOptionEntity, WherePageRequest<BomsProductConfigModelOptionEntity>> implements BomsProductConfigModelOptionDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductConfigModelOptionEntity> bomsProductConfigModelOptionEntityWherePageRequest, LambdaQueryWrapper<BomsProductConfigModelOptionEntity> queryWrapper) {
    }


    @Override
    public BomsProductConfigModelOptionEntity getByModelAndOptionCode(String modelCode, String optionCode) {
        LambdaQueryWrapper<BomsProductConfigModelOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getOptionCode, optionCode);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigModelOptionEntity> queryByModel(String modelCode) {
        LambdaQueryWrapper<BomsProductConfigModelOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getModelCode, modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigModelOptionEntity> queryProductConfigModelOptionByModelOrFeatureOrOptionCode(String modelCode,
                                                                                                              String featureCode,
                                                                                                              String optionCode,
                                                                                                              List<String> optionCodes) {

        LambdaQueryWrapper<BomsProductConfigModelOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getModelCode, modelCode);


        if (StringUtils.isNotBlank(featureCode)) {
            lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getFeatureCode, featureCode);
        }

        if (StringUtils.isNotBlank(optionCode)) {
            lambdaQueryWrapper.eq(BomsProductConfigModelOptionEntity::getOptionCode, optionCode);
        }

        if (CollectionUtils.isNotEmpty(optionCodes)) {
            lambdaQueryWrapper.in(BomsProductConfigModelOptionEntity::getOptionCode, optionCodes);
        }

        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
