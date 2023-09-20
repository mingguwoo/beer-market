package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.V36CodeLibraryChangeLogConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Repository
@RequiredArgsConstructor
public class V36CodeLibraryChangeLogRepositoryImpl implements V36CodeLibraryChangeLogRepository {

    private final BomsV36CodeLibraryChangeLogDao v36CodeLibraryChangeLogDao;
    private final V36CodeLibraryChangeLogConverter v36CodeLibraryChangeLogConverter;

    @Override
    public void save(V36CodeLibraryChangeLogAggr v36CodeLibraryChangeLogAggr) {

    }

    @Override
    public V36CodeLibraryChangeLogAggr find(Long aLong) {
        return null;
    }

    @Override
    public void batchSave(List<V36CodeLibraryChangeLogAggr> changeLogAggrList) {
        DaoSupport.batchSaveOrUpdate(v36CodeLibraryChangeLogDao, v36CodeLibraryChangeLogConverter.convertDoListToEntityList(changeLogAggrList));
    }

}
