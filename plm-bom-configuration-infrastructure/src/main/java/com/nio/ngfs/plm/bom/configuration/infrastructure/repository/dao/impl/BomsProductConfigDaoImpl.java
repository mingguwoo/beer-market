package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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

}
