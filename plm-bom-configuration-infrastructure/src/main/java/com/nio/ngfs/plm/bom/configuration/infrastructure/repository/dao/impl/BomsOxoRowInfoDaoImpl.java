package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.OxoInfoDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoRowInfoDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsOxoRowInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Slf4j
public class BomsOxoRowInfoDaoImpl  extends AbstractDao<BomsOxoRowInfoMapper,
        BomsOxoRowInfoEntity, WherePageRequest<BomsOxoRowInfoEntity>> implements BomsOxoRowInfoDao {



    @Override
    protected void fuzzyConditions(WherePageRequest<BomsOxoRowInfoEntity> bomsOxoRowInfoEntityWherePageRequest, LambdaQueryWrapper<BomsOxoRowInfoEntity> queryWrapper) {

    }

    @Override
    public void insertOxoRows(List<BomsOxoRowInfoEntity> bomsOxoRowInfoEntitys) {
        getBaseMapper().insertOxoRows(bomsOxoRowInfoEntitys);

    }

    @Override
    public  List<OxoInfoDo> queryFeatureListsByModel(String modelCode) {
        return getBaseMapper().queryFeatureListsByModel(modelCode);
    }
}
