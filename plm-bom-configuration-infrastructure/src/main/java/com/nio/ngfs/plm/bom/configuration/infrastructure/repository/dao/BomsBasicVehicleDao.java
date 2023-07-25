package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * @param modelCode
     * @param modelYear
     * @param regionOptionCode
     * @param driveHand
     * @param salesVersion
     * @return BaseVehicleAggr列表
     */
    List<BomsBasicVehicleEntity> queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(String modelCode, String modelYear, String regionOptionCode, String driveHand, String salesVersion);

    /**
     * 获取最新BaseVehicle记录
     *
     * @return BomsBasicVehicle
     */
    BomsBasicVehicleEntity getLastestBaseVehicle();

    /**
     * 根据Base Vehicle ID 获取 Base Vehicle
     *
     * @return BomsBasicVehicleEntity
     */
    BomsBasicVehicleEntity queryBaseVehicleByBaseVehicleId(String baseVehicleId);

    /**
     * 获取全量Base Vehicle
     *
     * @return List<BomsBasicVehicleEntity>
     */
    List<BomsBasicVehicleEntity> queryAll();

    List<BomsBasicVehicleEntity> queryByModel(String modelCode);
}
