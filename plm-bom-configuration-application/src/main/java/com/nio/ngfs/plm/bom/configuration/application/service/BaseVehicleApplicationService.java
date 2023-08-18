package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.EditBaseVehicleCmd;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
public interface BaseVehicleApplicationService {

    List<OxoOptionPackageAggr> EditBaseVehicleFilter (BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> aggrs, List<OxoFeatureOptionAggr> rows);

    List<OxoFeatureOptionAggr> queryRegionSalesDrivePoints(String modelCode);

    void addCopyFromPoints(AddBaseVehicleCmd cmd, BaseVehicleAggr baseVehicleAggr);

    void addBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr,List<OxoOptionPackageAggr> packages,AddBaseVehicleCmd cmd);

    void deleteBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr);

    void editBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr, EditBaseVehicleCmd cmd);

}
