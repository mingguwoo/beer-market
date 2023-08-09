package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductConfigOptionDaoImpl extends AbstractDao<BomsProductConfigOptionMapper, BomsProductConfigOptionEntity, WherePageRequest<BomsProductConfigOptionEntity>> implements BomsProductConfigOptionDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductConfigOptionEntity> bomsProductConfigOptionEntityWherePageRequest, LambdaQueryWrapper<BomsProductConfigOptionEntity> queryWrapper) {
    }

}
