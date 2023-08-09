package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigModelOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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

}
