package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductContextDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductContextMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
