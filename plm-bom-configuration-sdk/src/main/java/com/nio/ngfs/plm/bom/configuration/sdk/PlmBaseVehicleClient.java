package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmBaseVehicleClient {

    @PostMapping("/baseVehicle/queryBaseVehicle")
    ResultInfo<List<QueryBaseVehicleRespDto>>queryBaseVehicle(QueryBaseVehicleQry qry);

    @PostMapping("/baseVehicle/addBaseVehicle")
    ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/editBaseVehicle")
    ResultInfo<EditBaseVehicleRespDto> editBaseVehicle(EditBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/checkBaseVehicleStatus")
    ResultInfo<CheckBaseVehicleStatusRespDto> checkBaseVehicleStatus(CheckBaseVehicleStatusCmd cmd);

    @PostMapping("baseVehicle/deleteBaseVehicle")
    ResultInfo<DeleteBaseVehicleRespDto> deleteBaseVehicle(DeleteBaseVehicleCmd cmd);

}
