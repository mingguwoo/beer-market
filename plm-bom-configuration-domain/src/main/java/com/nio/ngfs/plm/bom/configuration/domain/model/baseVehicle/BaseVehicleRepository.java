package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BaseVehicleRepository extends Repository<BaseVehicleAggr,String> {

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
    List<BaseVehicleAggr> queryByModelModelYearRegionDriveHandSalesVersion(String model, String modelYear, String region, String driveHand, String salesVersion);


    /**
     * 根据车型
     * @param modelCode
     * @return
     */
    List<BaseVehicleAggr> queryByModel(String modelCode);

    BaseVehicleAggr queryBaseVehicleByBaseVehicleId(String baseVehicleId);
}
