package com.sh.beer.market.api.controller;

import com.sh.beer.market.sdk.dto.PlmBaseVehicleClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @date 2023/7/18
 */

@RestController
@Slf4j
@RequiredArgsConstructor
public class BaseVehicleController implements PlmBaseVehicleClient {
    @Override
    public void test() {
        log.info("test12333");
    }

    /*private final AddBaseVehicleCommand addBaseVehicleCommand;
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
    @NeedAuthorization
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
    }*/
}
