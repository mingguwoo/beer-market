package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.feature.*;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.GetChangeLogListQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.ListFeatureLibraryQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmFeatureClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class FeatureController implements PlmFeatureClient {

    private final AddGroupCommand addGroupCommand;
    private final EditGroupCommand editGroupCommand;
    private final ChangeGroupStatusCommand changeGroupStatusCommand;
    private final AddFeatureCommand addFeatureCommand;
    private final EditFeatureCommand editFeatureCommand;
    private final ChangeFeatureStatusCommand changeFeatureStatusCommand;
    private final ListFeatureLibraryQuery listFeatureLibraryQuery;
    private final AddOptionCommand addOptionCommand;
    private final EditOptionCommand editOptionCommand;
    private final ChangeOptionStatusCommand changeOptionStatusCommand;
    private final GetChangeLogListQuery getChangeLogListQuery;

    @Override
    @NotLogResult
    public ResultInfo<AddGroupRespDto> addGroup(@Valid @RequestBody AddGroupCmd cmd) {
        return ResultInfo.success(addGroupCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditGroupRespDto> editGroup(@Valid @RequestBody EditGroupCmd cmd) {
        return ResultInfo.success(editGroupCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<ChangeGroupStatusRespDto> changeGroupStatus(@Valid @RequestBody ChangeGroupStatusCmd cmd) {
        return ResultInfo.success(changeGroupStatusCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<AddFeatureRespDto> addFeature(@Valid @RequestBody AddFeatureCmd cmd) {
        return ResultInfo.success(addFeatureCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditFeatureCmdRespDto> editFeature(@Valid @RequestBody EditFeatureCmd cmd) {
        return ResultInfo.success(editFeatureCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<ChangeFeatureStatusRespDto> changeFeatureStatus(@Valid @RequestBody ChangeFeatureStatusCmd cmd) {
        return ResultInfo.success(changeFeatureStatusCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<FeatureLibraryDto>> listFeatureLibrary(@Valid @RequestBody ListFeatureLibraryQry qry) {
        return ResultInfo.success(listFeatureLibraryQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<AddOptionRespDto> addOption(@Valid @RequestBody AddOptionCmd cmd) {
        return ResultInfo.success(addOptionCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<EditOptionRespDto> editOption(@Valid @RequestBody EditOptionCmd cmd) {
        return ResultInfo.success(editOptionCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<ChangeOptionStatusRespDto> changeOptionStatus(@Valid @RequestBody ChangeOptionStatusCmd cmd) {
        return ResultInfo.success(changeOptionStatusCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<List<GetChangeLogListDto>> getChangeLogList(@Valid @RequestBody GetChangeLogListQry qry) {
        return ResultInfo.success(getChangeLogListQuery.execute(qry));
    }

}
