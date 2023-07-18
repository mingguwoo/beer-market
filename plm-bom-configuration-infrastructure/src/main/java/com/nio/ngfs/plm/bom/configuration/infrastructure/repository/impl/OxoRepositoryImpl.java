package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoRowInfoDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Repository
@RequiredArgsConstructor
public class OxoRepositoryImpl implements OxoRepository {


    private final BomsOxoRowInfoDao bomsOxoRowInfoDao;


    @Override
    public void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs) {
        bomsOxoRowInfoDao.insertOxoRows(BeanConvertUtils.convertListTo(rowInfoAggrs, BomsOxoRowInfoEntity::new));

    }

    @Override
    public void insertOxoOptionPackageInfo(List<OxoRowInfoAggr> rowInfoAggrs) {

        //bomsOxoRowInfoDao.insertOxoRows();

    }
}
