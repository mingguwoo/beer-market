package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.feature.AddGroupCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.feature.EditGroupCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.ListFeatureLibraryQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmFeatureClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDto;
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
    private final ListFeatureLibraryQuery listFeatureLibraryQuery;

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
    public ResultInfo<List<FeatureLibraryDto>> listFeatureLibrary(@Valid @RequestBody ListFeatureLibraryQry qry) {
        return ResultInfo.success(listFeatureLibraryQuery.execute(qry));
    }

}
