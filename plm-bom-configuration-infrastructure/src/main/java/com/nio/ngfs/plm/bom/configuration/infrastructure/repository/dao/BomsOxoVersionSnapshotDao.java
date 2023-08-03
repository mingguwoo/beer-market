package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoVersionSnapshotDao extends IService<BomsOxoVersionSnapshotEntity> {



    /**
     * 根据modelCode查询
     * @param modelCode
     * @return
     */
    List<BomsOxoVersionSnapshotEntity> queryBomsOxoVersionSnapshotsByModel(String modelCode);


    /**
     * 添加oxo 快照
     * @param entity
     */
    void insertBomsOxoVersionSnapshot(BomsOxoVersionSnapshotEntity entity);



}
