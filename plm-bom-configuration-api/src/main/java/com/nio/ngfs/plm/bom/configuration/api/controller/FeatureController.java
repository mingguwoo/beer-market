package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.feature.AddGroupCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.ListFeatureLibraryQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmFeatureClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@RestController
@Slf4j
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController implements PlmFeatureClient {

    private final AddGroupCommand addGroupCommand;
    private final ListFeatureLibraryQuery listFeatureLibraryQuery;

    @PostMapping("/addGroup")
    @Override
    @NeedAuthorization
    public ResultInfo<AddGroupRespDto> addGroup(@Valid @RequestBody AddGroupCmd request) {
        return ResultInfo.success(addGroupCommand.doAction(request));
    }

    @PostMapping("/listFeatureLibrary")
    @Override
    @NeedAuthorization
    public ResultInfo<List<FeatureLibraryDto>> listFeatureLibrary(@Valid @RequestBody ListFeatureLibraryQry request) {
        return ResultInfo.success(listFeatureLibraryQuery.doAction(request));
    }

}
