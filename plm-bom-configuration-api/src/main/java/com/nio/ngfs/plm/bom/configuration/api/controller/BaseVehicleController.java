package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.BaseVehicle.AddBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmBaseVehicleClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ListBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.AddBaseVehicleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class BaseVehicleController implements PlmBaseVehicleClient {

    private final AddBaseVehicleCommand addBaseVehicleCommand;

    @Override
    public ResultInfo<List<BaseVehicleDto>> listBaseVehicle(ListBaseVehicleQry qry) {
        return null;
    }

    @Override
    public ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(AddBaseVehicleCmd cmd) {
        return ResultInfo.success(addBaseVehicleCommand.execute(cmd));
    }
}
