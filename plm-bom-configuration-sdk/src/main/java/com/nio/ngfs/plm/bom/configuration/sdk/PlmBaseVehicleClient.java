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
    ResultInfo<List<BaseVehicleRespDto>>queryBaseVehicle(QueryBaseVehicleQry qry);

    @PostMapping("/baseVehicle/addBaseVehicle")
    ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/editBaseVehicle")
    ResultInfo<EditBaseVehicleRespDto> editBaseVehicle(EditBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/checkBaseVehicleStatus")
    ResultInfo<CheckBaseVehicleStatusRespDto> checkBaseVehicleStatus(CheckBaseVehicleStatusCmd cmd);

    @PostMapping("baseVehicle/deleteBaseVehicle")
    ResultInfo<DeleteBaseVehicleRespDto> deleteBaseVehicle(DeleteBaseVehicleCmd cmd);

    @PostMapping("baseVehicle/changeBaseVehicleStatus")
    ResultInfo<ChangeBaseVehicleStatusRespDto> changeBaseVehicleStatus(ChangeBaseVehicleStatusCmd cmd);

    @PostMapping("baseVehicle/getBaseVehicleOptions")
    ResultInfo<GetBaseVehicleOptionsRespDto> getBaseVehicleOptions(GetBaseVehicleOptionsQry qry);

    @PostMapping("baseVehicle/queryCopyFromModel")
    ResultInfo<List<BaseVehicleRespDto>> queryCopyFromModel(QueryCopyFromModelsQry qry);

    @PostMapping("baseVehicle/changeBaseVehicleMaturity")
    ResultInfo<ChangeBaseVehicleMaturityRespDto> changeBaseVehicleMaturity(ChangeBaseVehicleMaturityCmd cmd);
}
