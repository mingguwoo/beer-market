package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.EditBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ListBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.AddBaseVehicleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.EditBaseVehicleRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmBaseVehicleClient {

    @PostMapping("/baseVehicle/listBaseVehicle")
    ResultInfo<List<BaseVehicleDto>> listBaseVehicle(ListBaseVehicleQry qry);

    @PostMapping("/baseVehicle/addBaseVehicle")
    ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd);

    @PostMapping("/baseVehicle/editBaseVehicle")
    ResultInfo<EditBaseVehicleRespDto> editBaseVehicle(EditBaseVehicleCmd cmd);
}
