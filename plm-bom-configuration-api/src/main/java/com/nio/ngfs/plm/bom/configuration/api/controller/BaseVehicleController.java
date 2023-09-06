package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.basevehicle.AddBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.basevehicle.ChangeBaseVehicleStatusCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.basevehicle.DeleteBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.basevehicle.EditBaseVehicleCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.ExportBaseVehicleQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.GetBaseVehicleOptionsQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.QueryBaseVehicleQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.QueryCopyFromModelQuery;
import com.nio.ngfs.plm.bom.configuration.application.task.basevehicle.ImportBaseVehicleTask;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmBaseVehicleClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    private final EditBaseVehicleCommand editBaseVehicleCommand;
    private final DeleteBaseVehicleCommand deleteBaseVehicleCommand;
    private final QueryBaseVehicleQuery queryBaseVehicleQuery;
    private final ChangeBaseVehicleStatusCommand changeBaseVehicleStatusCommand;
    private final GetBaseVehicleOptionsQuery getbaseVehicleOptionsQuery;
    private final QueryCopyFromModelQuery queryCopyFromModelQuery;
    private final ExportBaseVehicleQuery exportBaseVehicleQuery;
    private final ImportBaseVehicleTask importBaseVehicleTask;


    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<BaseVehicleRespDto>> queryBaseVehicle(@Valid @RequestBody QueryBaseVehicleQry qry) {
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
    //@NeedAuthorization
    @NotLogResult
    public ResultInfo<DeleteBaseVehicleRespDto> deleteBaseVehicle(@Valid @RequestBody DeleteBaseVehicleCmd cmd) {
        return ResultInfo.success(deleteBaseVehicleCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<ChangeBaseVehicleStatusRespDto> changeBaseVehicleStatus(@Valid @RequestBody ChangeBaseVehicleStatusCmd cmd) {
        return ResultInfo.success(changeBaseVehicleStatusCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<GetBaseVehicleOptionsRespDto> getBaseVehicleOptions(@Valid @RequestBody GetBaseVehicleOptionsQry qry) {
        return ResultInfo.success(getbaseVehicleOptionsQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<BaseVehicleRespDto>> queryCopyFromModel(@Valid @RequestBody QueryCopyFromModelsQry qry) {
        return ResultInfo.success(queryCopyFromModelQuery.execute(qry));
    }

    @NeedAuthorization
    @NotLogResult
    @PostMapping("/baseVehicle/exportBaseVehicle")
    public void exportBaseVehicle(@Valid @RequestBody ExportBaseVehicleQry qry, HttpServletResponse response) {
        exportBaseVehicleQuery.execute(qry,response);
    }

    @NotLogResult
    @PostMapping("/baseVehicle/importBaseVehicle")
    public ResultInfo<ImportBaseVehicleRespDto> importBaseVehicleHistory(@RequestPart("file") MultipartFile file){
        return ResultInfo.success(importBaseVehicleTask.execute(file));
    }
}
