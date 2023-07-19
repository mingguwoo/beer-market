package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageInfoDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoRowInfoDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapShotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
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


    private final BomsOxoOptionPackageInfoDao oxoOptionPackageInfoDao;

    private final BomsOxoVersionSnapShotDao bomsOxoVersionSnapShotDao;


    @Override
    public void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs) {
        bomsOxoRowInfoDao.insertOxoRows(BeanConvertUtils.convertListTo(rowInfoAggrs, BomsOxoRowInfoEntity::new));

    }

    @Override
    public void insertOxoOptionPackageInfo(List<OxoPackageInfoAggr> oxoPackageInfoAggrs) {
        oxoOptionPackageInfoDao.insertOxoOptionPackageInfos(
                BeanConvertUtils.convertListTo(oxoPackageInfoAggrs, BomsOxoOptionPackageInfoEntity::new));

    }

    @Override
    public List<OxoVersionSnapshotAggr> queryOxoVersionSnapshotLists(String modelCode) {
        List<BomsOxoVersionSnapshotEntity> entities =
                bomsOxoVersionSnapShotDao.queryOxoVersionSnapshotLists(modelCode);
        return BeanConvertUtils.convertListTo(entities, OxoVersionSnapshotAggr::new);
    }


}
