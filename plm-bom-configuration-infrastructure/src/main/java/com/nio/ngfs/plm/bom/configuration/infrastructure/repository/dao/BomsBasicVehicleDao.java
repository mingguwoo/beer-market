package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BomsBasicVehicleDao extends IService<BomsBasicVehicleEntity> {

    /**
     * 根据Model,Model Year, Region, Drive Hand,Sales Version查找
     *
     * @param model
     * @param modelYear
     * @param region
     * @param driveHand
     * @param salesVersion
     * @return BaseVehicleAggr列表
     */
    List<BomsBasicVehicleEntity> queryByModelModelYearRegionDriveHandSalesVersion(String model, String modelYear, String region, String driveHand, String salesVersion);

    /**
     * 获取最新BaseVehicle记录
     *
     * @return BomsBasicVehicle
     */
    BomsBasicVehicleEntity getLastestBaseVehicle();
}
