package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.configurationrule.AddRuleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.configurationrule.DeleteGroupCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.configurationrule.DeleteRuleCommand;
import com.nio.ngfs.plm.bom.configuration.application.command.configurationrule.ReleaseRuleCommand;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.GetPurposeOptionListQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.QueryConfigurationRuleQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmConfigurationRuleClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.GetPurposeOptionListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.CheckRuleReleaseQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryConfigurationRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.AddRuleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.CheckRuleReleaseAvailableRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetPurposeOptionListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.QueryConfigurationRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@RestController
@RequiredArgsConstructor
public class ConfigurationRuleController implements PlmConfigurationRuleClient {

    private final AddRuleCommand addRuleCommand;
    private final DeleteGroupCommand deleteGroupCommand;
    private final DeleteRuleCommand deleteRuleCommand;
    private final ReleaseRuleCommand releaseRuleCommand;
    private final GetPurposeOptionListQuery getPurposeOptionListQuery;
    private final QueryConfigurationRuleQuery queryConfigurationRuleQuery;

    @Override
    @NotLogResult
    public ResultInfo<AddRuleRespDto> addRule(@Valid @RequestBody AddRuleCmd cmd) {
        return ResultInfo.success(addRuleCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<DeleteGroupRespDto> deleteGroup(@Valid @RequestBody DeleteGroupCmd cmd) {
        return ResultInfo.success(deleteGroupCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<DeleteRuleRespDto> deleteRule(@Valid @RequestBody DeleteRuleCmd cmd) {
        return ResultInfo.success(deleteRuleCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<ReleaseRuleRespDto> releaseRule(@Valid @RequestBody ReleaseRuleCmd cmd) {
        return ResultInfo.success(releaseRuleCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<List<GetPurposeOptionListRespDto>> getPurposeOptionList(@Valid @RequestBody GetPurposeOptionListQry qry) {
        return ResultInfo.success(getPurposeOptionListQuery.execute(qry));
    }

    @Override
    public ResultInfo<QueryConfigurationRuleRespDto> queryConfigurationRule(QueryConfigurationRuleQry qry) {
        return null;
    }

}
