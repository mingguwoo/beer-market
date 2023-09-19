package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.V36CodeLibraryConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common.DaoSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Repository
@RequiredArgsConstructor
public class V36CodeLibraryRepositoryImpl implements V36CodeLibraryRepository {

    private final BomsV36CodeLibraryDao bomsV36CodeLibraryDao;
    private final V36CodeLibraryConverter v36CodeLibraryConverter;

    @Override
    public void save(V36CodeLibraryAggr aggr) {
        DaoSupport.saveOrUpdate(bomsV36CodeLibraryDao, v36CodeLibraryConverter.convertDoToEntity(aggr), entity -> {
            if (aggr.getId() == null) {
                aggr.setId(entity.getId());
            }
        });
    }

    @Override
    public V36CodeLibraryAggr find(Long id) {
        return v36CodeLibraryConverter.convertEntityToDo(bomsV36CodeLibraryDao.getById(id));
    }

    @Override
    public List<V36CodeLibraryAggr> queryByParentCodeCodeAndChineseName(String parentCode, String code, String chineseName) {
        return v36CodeLibraryConverter.convertEntityListToDoList(bomsV36CodeLibraryDao.queryByParentCodeCodeAndChineseName(parentCode, code, chineseName));
    }

    @Override
    public List<V36CodeLibraryAggr> queryByParentId(Long parentId) {
        return v36CodeLibraryConverter.convertEntityListToDoList(bomsV36CodeLibraryDao.queryByParentId(parentId));
    }

}
