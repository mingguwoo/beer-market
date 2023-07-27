package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoVersionSnapshotDao extends IService<BomsOxoVersionSnapshotEntity> {

    /**
     *  校验BaseVehicle是否已发布
     * @param modelCode
     */
    boolean checkBaseVehicleStatus(String modelCode);

}
