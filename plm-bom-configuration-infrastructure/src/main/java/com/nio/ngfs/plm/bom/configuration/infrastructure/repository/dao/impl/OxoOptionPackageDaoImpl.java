package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.OxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoOptionPackageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@Slf4j
public class OxoOptionPackageDaoImpl extends AbstractDao<BomsOxoOptionPackageMapper, BomsOxoOptionPackageEntity, WherePageRequest<BomsOxoOptionPackageEntity>> implements OxoOptionPackageDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoOptionPackageEntity> bomsOxoOptionPackageEntityWherePageRequest, LambdaQueryWrapper<BomsOxoOptionPackageEntity> queryWrapper) {
    }

}
