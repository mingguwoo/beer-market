package com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import java.util.List;
import java.util.stream.Stream;


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



    public static List<String> convertOptionCodes(List<BaseVehicleAggr> baseVehicleAggrs){

        List<String> lists= Lists.newArrayList();

        baseVehicleAggrs.forEach(x->{
            lists.add(x.getRegionOptionCode());
            lists.add(x.getSalesVersion());
            lists.add(x.getDriveHand());
        });

        return lists.stream().distinct().toList();
    }

}
