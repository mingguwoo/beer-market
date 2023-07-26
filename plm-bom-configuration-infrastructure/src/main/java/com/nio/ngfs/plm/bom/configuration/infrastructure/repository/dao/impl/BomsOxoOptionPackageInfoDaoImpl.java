package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageInfoDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoOptionPackageInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Repository
@Slf4j
public class BomsOxoOptionPackageInfoDaoImpl extends AbstractDao<BomsOxoOptionPackageInfoMapper,
        BomsOxoOptionPackageInfoEntity, WherePageRequest<BomsOxoOptionPackageInfoEntity>> implements BomsOxoOptionPackageInfoDao {


    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoOptionPackageInfoEntity> bomsOxoOptionPackageInfoEntityWherePageRequest, LambdaQueryWrapper<BomsOxoOptionPackageInfoEntity> queryWrapper) {

    }

    @Override
    public void insertOxoOptionPackageInfos(List<BomsOxoOptionPackageInfoEntity> oxoOptionPackageInfos) {
        getBaseMapper().insertOxoOptionPackageInfos(oxoOptionPackageInfos);
    }

    @Override
    public List<BomsOxoOptionPackageInfoEntity> queryOxoOptionPackageByHeadIds(List<Long> rowIds) {
        LambdaQueryWrapper<BomsOxoOptionPackageInfoEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsOxoOptionPackageInfoEntity::getRowId, rowIds);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }
}
