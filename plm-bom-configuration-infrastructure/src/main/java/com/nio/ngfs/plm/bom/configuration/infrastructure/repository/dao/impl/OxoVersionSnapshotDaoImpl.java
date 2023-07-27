package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.OxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoVersionSnapshotMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@Slf4j
public class OxoVersionSnapshotDaoImpl extends AbstractDao<BomsOxoVersionSnapshotMapper, BomsOxoVersionSnapshotEntity, WherePageRequest<BomsOxoVersionSnapshotEntity>> implements OxoVersionSnapshotDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoVersionSnapshotEntity> bomsOxoVersionSnapshotEntityWherePageRequest, LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> queryWrapper) {
    }

}
