package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoVersionSnapshotMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Repository
@Slf4j
public class BomsOxoVersionSnapshotDaoImpl extends AbstractDao<BomsOxoVersionSnapshotMapper, BomsOxoVersionSnapshotEntity, WherePageRequest<BomsOxoVersionSnapshotEntity>> implements BomsOxoVersionSnapshotDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoVersionSnapshotEntity> bomsOxoVersionSnapshotEntityWherePageRequest, LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> queryWrapper) {
    }

    @Override
    public boolean checkBaseVehicleStatus(String modelCode) {
        return CollectionUtils.isNotEmpty(queryBomsOxoVersionSnapshotsByModel(modelCode));
    }

    @Override
    public List<BomsOxoVersionSnapshotEntity> queryBomsOxoVersionSnapshotsByModel(String modelCode) {
        LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getModelCode,modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public void insertBomsOxoVersionSnapshot(BomsOxoVersionSnapshotEntity entity) {
        getBaseMapper().insert(entity);
    }
}
