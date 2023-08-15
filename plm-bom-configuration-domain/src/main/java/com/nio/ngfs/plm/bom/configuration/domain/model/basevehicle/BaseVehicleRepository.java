package com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BaseVehicleRepository extends Repository<BaseVehicleAggr,String> {

    /**
     * 根据Model,Model Year, Region, Drive Hand,Sales Version查找 Base Vehicle
     *
     * @param modelCode
     * @param modelYear
     * @param regionOptionCode
     * @param driveHand
     * @param salesVersion
     * @return BaseVehicleAggr列表
     */
    List<BaseVehicleAggr> queryByModelCodeModelYearRegionDriveHandSalesVersion(String modelCode, String modelYear, String regionOptionCode, String driveHand, String salesVersion);


    /**
     * 根据车型
     * @param modelCode
     * @return
     */
    List<BaseVehicleAggr> queryByModel(String modelCode,Boolean isMaturity);

    /**
     * 根据Base Vehicle Id 查找 Base Vehicle
     *
     * @param baseVehicleId
     * @return BaseVehicleAggr
     */
    BaseVehicleAggr queryBaseVehicleByBaseVehicleId(String baseVehicleId);

    /**
     * 根据 Id 删除 Base Vehicle
     *
     * @param id
     */
    void removeById(Long id);

    /**
     * 根据 model和modelyear去查找
     *
     */
    List<BaseVehicleAggr> queryByModelCodeAndModelYear(String modelCode, String modelYear);

    /**
     * 批量存储
     *
     */
    void batchSave(List<BaseVehicleAggr>baseVehicleList);
}
