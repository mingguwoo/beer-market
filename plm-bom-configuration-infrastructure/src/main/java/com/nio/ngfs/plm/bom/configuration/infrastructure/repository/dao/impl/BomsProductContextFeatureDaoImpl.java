package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextFeatureDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductContextFeatureMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductContextFeatureDaoImpl extends AbstractDao<BomsProductContextFeatureMapper, BomsProductContextFeatureEntity, WherePageRequest<BomsProductContextFeatureEntity>> implements BomsProductContextFeatureDao{
    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductContextFeatureEntity> bomsProductContextFeatureEntityWherePageRequest, LambdaQueryWrapper<BomsProductContextFeatureEntity> queryWrapper) {

    }
}
