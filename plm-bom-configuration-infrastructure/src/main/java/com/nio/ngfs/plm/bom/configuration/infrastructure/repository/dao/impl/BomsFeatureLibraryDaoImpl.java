package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsFeatureLibraryMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.dromara.dynamictp.core.thread.DtpExecutor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
@Repository
@Slf4j
public class BomsFeatureLibraryDaoImpl extends AbstractDao<BomsFeatureLibraryMapper, BomsFeatureLibraryEntity, WherePageRequest<BomsFeatureLibraryEntity>> implements BomsFeatureLibraryDao {

    @Resource
    private DtpExecutor ioThreadPool;

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsFeatureLibraryEntity> bomsFeatureLibraryEntityWherePageRequest, LambdaQueryWrapper<BomsFeatureLibraryEntity> queryWrapper) {
    }

    @Override
    public BomsFeatureLibraryEntity getByFeatureCodeAndType(String featureCode, String type) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getFeatureCode, featureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, type);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsFeatureLibraryEntity> queryByParentFeatureCodeAndType(String parentFeatureCode, String type) {
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getParentFeatureCode, parentFeatureCode);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, type);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsFeatureLibraryEntity> queryByParentFeatureCodeListAndType(List<String> parentFeatureCodeList, String type) {
        if (CollectionUtils.isEmpty(parentFeatureCodeList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsFeatureLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsFeatureLibraryEntity::getParentFeatureCode, parentFeatureCodeList);
        lambdaQueryWrapper.eq(BomsFeatureLibraryEntity::getType, type);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsFeatureLibraryEntity> queryAll() {
        return getBaseMapper().selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public void batchUpdateStatus(List<Long> idList, String status) {
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        LambdaQueryWrapper<BomsFeatureLibraryEntity> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.in(BomsFeatureLibraryEntity::getId, idList);
        BomsFeatureLibraryEntity updateEntity = new BomsFeatureLibraryEntity();
        updateEntity.setStatus(status);
        getBaseMapper().update(updateEntity, updateWrapper);
    }

    /**
     * 测试动态线程池使用
     */
    public List<BomsFeatureLibraryEntity> batchQuery() {
        Future<List<BomsFeatureLibraryEntity>> queryFuture = ioThreadPool.submit(() -> getBaseMapper().selectList(new LambdaQueryWrapper<>()));
        try {
            return queryFuture.get();
        } catch (ExecutionException | InterruptedException ex) {
            log.error("Encountered exception: {} ", ex.getMessage());
        }
        return Lists.newArrayList();
    }

}
