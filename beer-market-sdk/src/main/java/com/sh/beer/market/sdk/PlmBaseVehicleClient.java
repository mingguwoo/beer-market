package com.sh.beer.market.sdk;


import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author
 * @date 2023/7/18
 */
@FeignClient(name = "beer-market")
public interface PlmBaseVehicleClient {

    /*@PostMapping("/baseVehicle/queryBaseVehicle")
    ResultInfo<List<BaseVehicleRespDto>>queryBaseVehicle(QueryBaseVehicleQry qry);

    @PostMapping("/baseVehicle/addBaseVehicle")
    ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/editBaseVehicle")
    ResultInfo<EditBaseVehicleRespDto> editBaseVehicle(EditBaseVehicleCmd cmd);

    @PostMapping("baseVehicle/deleteBaseVehicle")
    ResultInfo<DeleteBaseVehicleRespDto> deleteBaseVehicle(DeleteBaseVehicleCmd cmd);

    @PostMapping("baseVehicle/changeBaseVehicleStatus")
    ResultInfo<ChangeBaseVehicleStatusRespDto> changeBaseVehicleStatus(ChangeBaseVehicleStatusCmd cmd);

    @PostMapping("baseVehicle/getBaseVehicleOptions")
    ResultInfo<GetBaseVehicleOptionsRespDto> getBaseVehicleOptions(GetBaseVehicleOptionsQry qry);

    @PostMapping("baseVehicle/queryCopyFromModel")
    ResultInfo<List<BaseVehicleRespDto>> queryCopyFromModel(QueryCopyFromModelsQry qry);*/

}
