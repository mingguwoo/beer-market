package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.bom.share.annotation.NotLogResult;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.application.command.configurationrule.*;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.ExportConfigurationRuleQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.GetPurposeOptionListQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.QueryConfigurationRuleQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.QueryConfigurationRuleViewQuery;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmConfigurationRuleClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
    private final EditGroupAndRuleCommand editGroupAndRuleCommand;
    private final DeleteGroupCommand deleteGroupCommand;
    private final DeleteRuleCommand deleteRuleCommand;
    private final ReleaseRuleCommand releaseRuleCommand;
    private final GetPurposeOptionListQuery getPurposeOptionListQuery;
    private final QueryConfigurationRuleQuery queryConfigurationRuleQuery;
    private final QueryConfigurationRuleViewQuery queryConfigurationRuleViewQuery;
    private final ReviseRuleCommand reviseRuleCommand;
    private final RemoveRuleCommand removeRuleCommand;
    private final ExportConfigurationRuleQuery exportConfigurationRuleQuery;

    @Override
    @NotLogResult
    public ResultInfo<AddRuleRespDto> addRule(@Valid @RequestBody AddRuleCmd cmd) {
        return ResultInfo.success(addRuleCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<EditGroupAndRuleRespDto> editGroupAndRule(@Valid @RequestBody EditGroupAndRuleCmd cmd) {
        return ResultInfo.success(editGroupAndRuleCommand.execute(cmd));
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
    @NotLogResult
    public ResultInfo<QueryConfigurationRuleRespDto> queryConfigurationRule(@Valid @RequestBody QueryConfigurationRuleQry qry) {
        return ResultInfo.success(queryConfigurationRuleQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<ReviseRuleRespDto> reviseRule(@Valid @RequestBody ReviseRuleCmd cmd) {
        return ResultInfo.success(reviseRuleCommand.execute(cmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<Boolean> remove(RemoveRuleCmd removeRuleCmd) {
        return ResultInfo.success(removeRuleCommand.execute(removeRuleCmd));
    }

    @NotLogResult
    @PostMapping("/configurationRule/exportConfigurationRule")
    public void exportConfigurationRule(@Valid @RequestBody ExportConfigurationRuleQry qry, HttpServletResponse response) {
            exportConfigurationRuleQuery.execute(qry,response);
    }

    @Override
    @NotLogResult
    public ResultInfo<RuleViewInfoRespDto> view(QueryViewQry qry) {
        return ResultInfo.success(queryConfigurationRuleViewQuery.execute(qry));
    }


}
