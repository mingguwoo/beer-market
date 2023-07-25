package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle.AddBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle.CheckBaseVehicleStatusCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle.DeleteBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle.EditBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.QueryBaseVehicleQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmBaseVehicleClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author bill.wang
 * @date 2023/7/18
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class BaseVehicleController implements PlmBaseVehicleClient {

    private final AddBaseVehicleCommand addBaseVehicleCommand;
    private final EditBaseVehicleCommand editBaseVehicleCommand;
    private final CheckBaseVehicleStatusCommand checkBaseVehicleStatusCommand;
    private final DeleteBaseVehicleCommand deleteBaseVehicleCommand;
    private final QueryBaseVehicleQuery queryBaseVehicleQuery;


    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<QueryBaseVehicleRespDto> queryBaseVehicle(@Valid @RequestBody QueryBaseVehicleQry qry) {
        return ResultInfo.success(queryBaseVehicleQuery.executeQuery(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<AddBaseVehicleRespDto> addBaseVehicle(@Valid @RequestBody AddBaseVehicleCmd cmd) {
        return ResultInfo.success(addBaseVehicleCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<EditBaseVehicleRespDto> editBaseVehicle(@Valid @RequestBody EditBaseVehicleCmd cmd) {
        return ResultInfo.success(editBaseVehicleCommand.execute(cmd));
    }

    @Override
//    @NeedAuthorization
    @NotLogResult
    public ResultInfo<CheckBaseVehicleStatusRespDto> checkBaseVehicleStatus(@Valid @RequestBody CheckBaseVehicleStatusCmd cmd) {
        return ResultInfo.success(checkBaseVehicleStatusCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<DeleteBaseVehicleRespDto> deleteBaseVehicle(@Valid @RequestBody DeleteBaseVehicleCmd cmd) {
        return ResultInfo.success(deleteBaseVehicleCommand.execute(cmd));
    }
    
}
