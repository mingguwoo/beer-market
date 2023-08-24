package com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.OxoBasicVehicleDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;

import java.util.Comparator;
import java.util.List;


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


    public static List<OxoBasicVehicleDto> buildOxoBasicVehicles(List<OxoHeadQry> oxoHeads, String modelCode, String version) {

        List<OxoBasicVehicleDto> oxoBasicVehicleDtos = Lists.newArrayList();
        oxoHeads.forEach(oxoHeadQry -> {
            oxoHeadQry.getRegionInfos().forEach(regionInfo -> {
                regionInfo.getDriveHands().forEach(driveHandInfo -> {
                    driveHandInfo.getSalesVersionInfos().forEach(salesVersionInfo -> {

                        OxoBasicVehicleDto oxoBasicVehicleDto = new OxoBasicVehicleDto();
                        oxoBasicVehicleDto.setSalesOption(salesVersionInfo.getSalesName());
                        oxoBasicVehicleDto.setRegion(regionInfo.getRegionName());
                        oxoBasicVehicleDto.setDriverOption(driveHandInfo.getDriveHandName());

                        oxoBasicVehicleDto.setRegionCode(regionInfo.getRegionCode());
                        oxoBasicVehicleDto.setDriverOptionCode(driveHandInfo.getDriveHandCode());
                        oxoBasicVehicleDto.setSalesOptionCode(salesVersionInfo.getSalesCode());
                        oxoBasicVehicleDto.setModelYear(modelCode + " " + oxoHeadQry.getModelYear() + " " + version);
                        oxoBasicVehicleDto.setYear(oxoHeadQry.getModelYear());
                        oxoBasicVehicleDtos.add(oxoBasicVehicleDto);
                    });
                });
            });
        });
        return oxoBasicVehicleDtos;
    }

}
