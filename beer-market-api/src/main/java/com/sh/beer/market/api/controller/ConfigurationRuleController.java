package com.sh.beer.market.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @date 2023/10/17
 */
@RestController
@RequiredArgsConstructor
public class ConfigurationRuleController {

    /*private final AddRuleCommand addRuleCommand;
    private final EditGroupAndRuleCommand editGroupAndRuleCommand;
    private final DeleteGroupCommand deleteGroupCommand;
    private final DeleteRuleCommand deleteRuleCommand;
    private final ReleaseRuleCommand releaseRuleCommand;
    private final GetGroupAndRuleQuery getGroupAndRuleQuery;
    private final GetPurposeOptionListQuery getPurposeOptionListQuery;
    private final QueryConfigurationRuleQuery queryConfigurationRuleQuery;
    private final QueryConfigurationRuleViewQuery queryConfigurationRuleViewQuery;
    private final ReviseRuleCommand reviseRuleCommand;
    private final RemoveRuleCommand removeRuleCommand;
    private final SetBreakPointCommand setBreakPointCommand;
    private final BreakPointCheckCommand breakPointCheckCommand;
    private final ExportConfigurationRuleQuery exportConfigurationRuleQuery;
    private final ExportConfigurationRuleViewQuery exportConfigurationRuleViewQuery;

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
    public ResultInfo<GetGroupAndRuleRespDto> getGroupAndRule(@Valid @RequestBody GetGroupAndRuleQry qry) {
        return ResultInfo.success(getGroupAndRuleQuery.execute(qry));
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
    public ResultInfo<Boolean> remove(@Valid @RequestBody RemoveRuleCmd removeRuleCmd) {
        return ResultInfo.success(removeRuleCommand.execute(removeRuleCmd));
    }

    @NotLogResult
    @PostMapping("/configurationRule/exportConfigurationRule")
    public void exportConfigurationRule(@Valid @RequestBody ExportConfigurationRuleQry qry, HttpServletResponse response) {
        exportConfigurationRuleQuery.execute(qry, response);
    }

    @Override
    @NotLogResult
    public ResultInfo<RuleViewInfoRespDto> view(@Valid @RequestBody QueryViewQry qry) {
        return ResultInfo.success(queryConfigurationRuleViewQuery.execute(qry));
    }

    @Override
    @NotLogResult
    public ResultInfo<Boolean> setBreakPoint(@Valid @RequestBody SetBreakPointCmd setBreakPointCmd) {
        return ResultInfo.success(setBreakPointCommand.execute(setBreakPointCmd));
    }

    @Override
    @NotLogResult
    public ResultInfo<RuleViewCheckRespDto> breakPointCheck(@RequestBody BreakPointCheckCmd breakPointCheckCmd) {
        return ResultInfo.success(breakPointCheckCommand.execute(breakPointCheckCmd));
    }


    @PostMapping("/configurationRule/exportView")
    @NotLogResult
    public void exportView(@Valid @RequestBody QueryViewQry qry, HttpServletResponse response, HttpServletRequest request) {
        exportConfigurationRuleViewQuery.execute(qry, response, request);
    }*/
}
