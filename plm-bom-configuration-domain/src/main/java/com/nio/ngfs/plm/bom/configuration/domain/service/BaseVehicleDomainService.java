package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;

/**
 * @author bill.wang
 * @date 2023/7/19
 */
public interface BaseVehicleDomainService {

    /**
     * 校验Base Vehicle 是否唯一
     *
     * @param baseVehicleAggr 聚合根
     */
    void checkBaseVehicleUnique(BaseVehicleAggr baseVehicleAggr);

}
