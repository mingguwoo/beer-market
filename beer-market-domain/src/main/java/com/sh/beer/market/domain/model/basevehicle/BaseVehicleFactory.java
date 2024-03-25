package com.sh.beer.market.domain.model.basevehicle;

/**
 * @author
 * @date 2023/7/18
 */
public class BaseVehicleFactory {

    /*public static BaseVehicleAggr createBaseVehicle(AddBaseVehicleCmd cmd){
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
                        oxoBasicVehicleDto.setId(salesVersionInfo.getHeadId());
                        oxoBasicVehicleDto.setRegionCode(regionInfo.getRegionCode());
                        oxoBasicVehicleDto.setDriverOptionCode(driveHandInfo.getDriveHandCode());
                        oxoBasicVehicleDto.setSalesOptionCode(salesVersionInfo.getSalesCode());
                        oxoBasicVehicleDto.setModelYear(modelCode + " " + oxoHeadQry.getModelYear() + " " + version);
                        oxoBasicVehicleDto.setYear(oxoHeadQry.getModelYear());
                        oxoBasicVehicleDto.setChangeType(salesVersionInfo.getChangeType());
                        oxoBasicVehicleDtos.add(oxoBasicVehicleDto);
                    });
                });
            });
        });
        return oxoBasicVehicleDtos;
    }*/

}
