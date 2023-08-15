package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BomsBaseVehicleDao extends IService<BomsBaseVehicleEntity> {

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
    List<BomsBaseVehicleEntity> queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(String modelCode, String modelYear, String regionOptionCode, String driveHand, String salesVersion);

    /**
     * 获取最新BaseVehicle记录的id
     *
     * @return id
     */
    Long getLastestBaseVehicle();

    /**
     * 根据Base Vehicle ID 获取 Base Vehicle
     *
     * @return BomsBasicVehicleEntity
     */
    BomsBaseVehicleEntity queryBaseVehicleByBaseVehicleId(String baseVehicleId);

    /**
     * 获取全量Base Vehicle
     *
     * @return List<BomsBasicVehicleEntity>
     */
    List<BomsBaseVehicleEntity> queryAll();

    List<BomsBaseVehicleEntity> queryByModel(String modelCode);

    /**
     * 获取同一ModeCode下Status为active的Model
     *
     * @return List<BomsBasicVehicleEntity>
     */
    List<BomsBaseVehicleEntity> queryCopyFromModel(String modelCode);

    List<BomsBaseVehicleEntity> queryByModelCodeAndModelYear(String modelCode, String modelYear);
}
