package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public class BaseVehicleFactory {
    public static BaseVehicleAggr createBaseVehicle(AddBaseVehicleCmd cmd){
        return BaseVehicleAggr.builder()
                .baseVehicleId(createBaseVehicleId())
                .model(cmd.getModel())
                .modelYear(cmd.getModelYear())
                .region(cmd.getRegion())
                .regionEn(cmd.getRegionEn())
                .regionCn(cmd.getRegionCn())
                .driveHand(cmd.getDriveHand())
                .driveHandEn(cmd.getDriveHandEn())
                .driveHandCn(cmd.getDriveHandCn())
                .salesVersion(cmd.getSalesVersion())
                .salesVersionEn(cmd.getSalesVersionEn())
                .salesVersionCn(cmd.getSalesVersionCn())
                .owner(cmd.getCreateUser())
                .build();
    }
    public static String createBaseVehicleId() {
        return null;
    }
}
