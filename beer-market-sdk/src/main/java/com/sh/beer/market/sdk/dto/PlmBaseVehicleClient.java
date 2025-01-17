package com.sh.beer.market.sdk.dto;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author
 * @date 2023/7/18
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmBaseVehicleClient {

    @PostMapping("/baseVehicle/test")
    void test();

    /*@PostMapping("/baseVehicle/queryBaseVehicle")
    ResultInfo<List<BaseVehicleRespDto>>queryBaseVehicle(QueryBaseVehicleQry qry);

    @PostMapping("/baseVehicle/addBaseVehicle")
    ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd);

    /*@PostMapping("/baseVehicle/editBaseVehicle")
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
