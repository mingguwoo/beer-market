package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureChangeLogMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Repository
public class BomsFeatureChangeLogDaoImpl extends AbstractDao<BomsFeatureChangeLogMapper, BomsFeatureChangeLogEntity, WherePageRequest<BomsFeatureChangeLogEntity>> implements BomsFeatureChangeLogDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsFeatureChangeLogEntity> bomsFeatureChangeLogEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureChangeLogEntity> queryWrapper) {
    }

}
