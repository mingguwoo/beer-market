package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmConfigurationRuleClient {

    /**
     * 新增Rule
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/configurationRule/addRule")
    ResultInfo<AddRuleRespDto> addRule(AddRuleCmd cmd);

    /**
     * 编辑Group和Rule
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/configurationRule/editGroupAndRule")
    ResultInfo<EditGroupAndRuleRespDto> editGroupAndRule(EditGroupAndRuleCmd cmd);

    /**
     * 删除Group
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/configurationRule/deleteGroup")
    ResultInfo<DeleteGroupRespDto> deleteGroup(DeleteGroupCmd cmd);

    /**
     * 删除Rule
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/configurationRule/deleteRule")
    ResultInfo<DeleteRuleRespDto> deleteRule(DeleteRuleCmd cmd);

    /**
     * 发布Rule
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/configurationRule/releaseRule")
    ResultInfo<ReleaseRuleRespDto> releaseRule(ReleaseRuleCmd cmd);

    /**
     * 获取Group和Rule详情
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/configurationRule/getGroupAndRule")
    ResultInfo<GetGroupAndRuleRespDto> getGroupAndRule(GetGroupAndRuleQry qry);

    /**
     * 查询Purpose选项列表
     */
    @PostMapping("/configurationRule/getPurposeOptionList")
    ResultInfo<List<GetPurposeOptionListRespDto>> getPurposeOptionList(GetPurposeOptionListQry qry);

    /**
     * 查询group和rule
     * @param qry
     * @return
     */
    @PostMapping("/configurationRule/queryConfigurationRule")
    ResultInfo<QueryConfigurationRuleRespDto> queryConfigurationRule(QueryConfigurationRuleQry qry);

    /**
     * rule升版
     * @param cmd
     * @return
     */
    @PostMapping("/configurationRule/reviseRule")
    ResultInfo<ReviseRuleRespDto> reviseRule(ReviseRuleCmd cmd);


    /**
     * 删除 对当前Rev的Rule条目进行工程端的失效处理
     * @param removeRuleCmd
     * @return
     */
    @PostMapping("/configurationRule/remove")
    ResultInfo<Boolean> remove(RemoveRuleCmd removeRuleCmd);


    /**
     * view视图页
     * @param qry
     * @return
     */
    @PostMapping("/configurationRule/view")
    ResultInfo<RuleViewInfoRespDto> view(QueryViewQry qry);

    /**
     * 给Rule条目设置Eff-in、Eff-out时间
     * @param setBreakPointCmd
     * @return
     */
    @PostMapping("/configurationRule/setBreakPoint")
    ResultInfo<Boolean> setBreakPoint(SetBreakPointCmd setBreakPointCmd);


    @PostMapping("/configurationRule/breakPointCheck")
    ResultInfo<RuleViewCheckRespDto> breakPointCheck();
}
