package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoVersionSnapshotMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public BomsOxoVersionSnapshotEntity queryLastReleaseSnapshotByModel(String modelCode, String type) {
        LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getModelCode, modelCode);
        lambdaQueryWrapper.eq(type != null, BomsOxoVersionSnapshotEntity::getType, type);
        lambdaQueryWrapper.orderByDesc(BomsOxoVersionSnapshotEntity::getVersion);
        lambdaQueryWrapper.last("limit 1");
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsOxoVersionSnapshotEntity> queryBomsOxoVersionSnapshotsByModelOrVersionOrType(
            String modelCode, String version, String type) {
        LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getModelCode, modelCode);

        if (StringUtils.isNotBlank(version)) {
            lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getVersion, version);
        }

        if (StringUtils.isNotBlank(type)) {
            lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getType, type);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public void insertBomsOxoVersionSnapshot(BomsOxoVersionSnapshotEntity entity) {
        getBaseMapper().insert(entity);
    }

    @Override
    public Page<BomsOxoVersionSnapshotEntity> querySnapshotByModel(String modelCode, String type, Integer pageSize, Integer pageNum) {


        LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BomsOxoVersionSnapshotEntity::getModelCode, modelCode);
        queryWrapper.eq(BomsOxoVersionSnapshotEntity::getType, type);

        Page<BomsOxoVersionSnapshotEntity> page = new Page<>(pageNum, pageSize);
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    public List<BomsOxoVersionSnapshotEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }
}
