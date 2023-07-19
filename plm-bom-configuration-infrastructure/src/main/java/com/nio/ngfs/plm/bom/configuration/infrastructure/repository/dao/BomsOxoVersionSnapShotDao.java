package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface BomsOxoVersionSnapShotDao extends IService<BomsOxoVersionSnapshotEntity> {


    /**
     * 根据车型 查询 快照信息
     * @param modelCode
     * @return
     */
    List<BomsOxoVersionSnapshotEntity> queryOxoVersionSnapshotLists(String modelCode);








}
