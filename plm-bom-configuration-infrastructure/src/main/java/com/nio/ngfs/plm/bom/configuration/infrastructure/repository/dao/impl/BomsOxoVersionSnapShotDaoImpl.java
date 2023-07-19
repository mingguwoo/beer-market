package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapShotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoVersionSnapshotMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Repository
@Slf4j
public class BomsOxoVersionSnapShotDaoImpl extends AbstractDao<BomsOxoVersionSnapshotMapper,
        BomsOxoVersionSnapshotEntity, WherePageRequest<BomsOxoVersionSnapshotEntity>> implements BomsOxoVersionSnapShotDao {







    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoVersionSnapshotEntity> bomsOxoVersionSnapshotEntityWherePageRequest, LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> queryWrapper) {

    }

    @Override
    public List<BomsOxoVersionSnapshotEntity> queryOxoVersionSnapshotLists(String modelCode) {

        LambdaQueryWrapper<BomsOxoVersionSnapshotEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoVersionSnapshotEntity::getModelCode, modelCode);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }
}
