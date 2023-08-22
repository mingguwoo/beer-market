package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoVersionSnapshotDao extends IService<BomsOxoVersionSnapshotEntity> {

    /**
     * 根据车型查询最新Release的OXO版本
     *
     * @param modelCode 车型
     * @param type 类型
     * @return BomsOxoVersionSnapshotEntity
     */
    BomsOxoVersionSnapshotEntity queryLastReleaseSnapshotByModel(String modelCode, String type);

    /**
     * 根据modelCode查询
     * @param modelCode
     * @return
     */
    List<BomsOxoVersionSnapshotEntity> queryBomsOxoVersionSnapshotsByModelOrVersionOrType(
            String modelCode,String version,String type);


    /**
     * 添加oxo 快照
     * @param entity
     */
    void insertBomsOxoVersionSnapshot(BomsOxoVersionSnapshotEntity entity);


    /**
     * 根据 车型 分页查找
     * @param modelCode
     * @return
     */
    Page<BomsOxoVersionSnapshotEntity> querySnapshotByModel(String modelCode,String type,Integer pageSize,Integer pageNum);
}
