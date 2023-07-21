package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;


/**
 * @author bill.wang
 * @date 2023/7/18
 */
public class BaseVehicleFactory {

    public static BaseVehicleAggr createBaseVehicle(AddBaseVehicleCmd cmd){
        return BaseVehicleAggr.builder()
                .modelCode(cmd.getModelCode())
                .modelYear(cmd.getModelYear())
                .regionOptionCode(cmd.getRegionOptionCode())
                .driveHand(cmd.getDriveHand())
                .salesVersion(cmd.getSalesVersion())
                .createUser(cmd.getCreateUser())
                .build();
    }

}
