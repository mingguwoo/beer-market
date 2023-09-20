package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsV36CodeLibraryChangeLogMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Repository
public class BomsV36CodeLibraryChangeLogDaoImpl extends AbstractDao<BomsV36CodeLibraryChangeLogMapper, BomsV36CodeLibraryChangeLogEntity, WherePageRequest<BomsV36CodeLibraryChangeLogEntity>>
        implements BomsV36CodeLibraryChangeLogDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsV36CodeLibraryChangeLogEntity> bomsV36CodeLibraryChangeLogEntityWherePageRequest, LambdaQueryWrapper<BomsV36CodeLibraryChangeLogEntity> queryWrapper) {
    }

}
