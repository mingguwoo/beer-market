package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.feature.*;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.*;
import com.nio.ngfs.plm.bom.configuration.application.task.feature.ImportFeatureLibraryTask;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmFeatureClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.*;
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
    private final AddOptionCommand addOptionCommand;
    private final EditOptionCommand editOptionCommand;
    private final ChangeOptionStatusCommand changeOptionStatusCommand;
    private final FullSyncToEnoviaCommand fullSyncToEnoviaCommand;
    private final GetChangeLogListQuery getChangeLogListQuery;
    private final GetGroupCodeListQuery getGroupCodeListQuery;
    private final QueryFeatureLibraryQuery queryFeatureLibraryQuery;
    private final ExportFeatureLibraryQuery exportFeatureLibraryQuery;
    private final ImportFeatureLibraryTask importFeatureLibraryTask;
    private final QueryFeatureCodeByCatalogQuery queryFeatureCodeByCatalogQuery;

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<AddGroupRespDto> addGroup(@Valid @RequestBody AddGroupCmd cmd) {
        return ResultInfo.success(addGroupCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<EditGroupRespDto> editGroup(@Valid @RequestBody EditGroupCmd cmd) {
        return ResultInfo.success(editGroupCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<ChangeGroupStatusRespDto> changeGroupStatus(@Valid @RequestBody ChangeGroupStatusCmd cmd) {
        return ResultInfo.success(changeGroupStatusCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<AddFeatureRespDto> addFeature(@Valid @RequestBody AddFeatureCmd cmd) {
        return ResultInfo.success(addFeatureCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<EditFeatureCmdRespDto> editFeature(@Valid @RequestBody EditFeatureCmd cmd) {
        return ResultInfo.success(editFeatureCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<ChangeFeatureStatusRespDto> changeFeatureStatus(@Valid @RequestBody ChangeFeatureStatusCmd cmd) {
        return ResultInfo.success(changeFeatureStatusCommand.execute(cmd));
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
    public ResultInfo<FullSyncToEnoviaRespDto> fullSyncToEnovia(@Valid @RequestBody FullSyncToEnoviaCmd cmd) {
        return ResultInfo.success(fullSyncToEnoviaCommand.execute(cmd));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<GetChangeLogListDto>> getChangeLogList(@Valid @RequestBody GetChangeLogListQry qry) {
        return ResultInfo.success(getChangeLogListQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<String>> getGroupCodeList(@Valid @RequestBody GetGroupCodeListQry qry) {
        return ResultInfo.success(getGroupCodeListQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<QueryFeatureLibraryDto>> queryFeatureLibrary(@Valid @RequestBody QueryFeatureLibraryQry qry) {
        return ResultInfo.success(queryFeatureLibraryQuery.execute(qry));
    }

    @Override
    @NeedAuthorization
    @NotLogResult
    public ResultInfo<List<QueryFeatureLibraryDto>> getFeatureByCatalog(@Valid @RequestBody QueryFeatureCodeByCatalogQry qry) {
        return ResultInfo.success(queryFeatureCodeByCatalogQuery.executeQuery(qry));
    }

    /**
     * 导出Feature Library到Excel
     */
    @NeedAuthorization
    @NotLogResult
    @PostMapping("/feature/exportFeatureLibrary")
    public void exportFeatureLibrary(@Valid @RequestBody ExportFeatureLibraryQry qry, HttpServletResponse response) {
        exportFeatureLibraryQuery.execute(qry, response);
    }

    /**
     * 导入Feature Library历史数据
     */
    @NotLogResult
    @PostMapping("/feature/importFeatureLibrary")
    public ResultInfo<ImportFeatureLibraryRespDto> importFeatureLibrary(@RequestPart("file") MultipartFile file) {
        return ResultInfo.success(importFeatureLibraryTask.execute(file));
    }

}
