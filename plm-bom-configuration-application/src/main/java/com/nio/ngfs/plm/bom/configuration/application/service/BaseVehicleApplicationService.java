package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.DeleteBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.EditBaseVehicleCmd;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
public interface BaseVehicleApplicationService {

    /**
     * 筛选得到要更新的点
     * @param baseVehicleAggr
     * @param aggrs
     * @param rows
     * @param editPoints
     * @param cmd
     */
    void EditBaseVehicleFilter(BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> aggrs, List<OxoFeatureOptionAggr> rows,List<OxoOptionPackageAggr> editPoints,EditBaseVehicleCmd cmd);

    /**
     * 获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
     * @param modelCode
     * @return
     */
    List<OxoFeatureOptionAggr> queryRegionSalesDrivePoints(String modelCode);

    /**
     * 增加copyFrom的点
     * @param cmd
     * @param baseVehicleAggr
     */
    void addCopyFromPoints(AddBaseVehicleCmd cmd, BaseVehicleAggr baseVehicleAggr);

    /**
     * 将addBaseVehicle数据存库并将copyFrom的点存库
     * @param baseVehicleAggr
     * @param packages
     * @param cmd
     */
    void addBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr,List<OxoOptionPackageAggr> packages,AddBaseVehicleCmd cmd);

    /**
     * 将要删除的baseVehicle数据更新库
     * @param baseVehicleAggr
     */
    void deleteBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr);

    /**
     * 编辑BaseVehicle并更新oxo打点
     * @param baseVehicleAggr
     * @param cmd
     */
    void editBaseVehicleAndOxo(BaseVehicleAggr baseVehicleAggr, EditBaseVehicleCmd cmd);

    /**
     * 校验baseVehicle是否已经发布
     * @param cmd
     */
    void checkBaseVehicleReleased(DeleteBaseVehicleCmd cmd);

}
