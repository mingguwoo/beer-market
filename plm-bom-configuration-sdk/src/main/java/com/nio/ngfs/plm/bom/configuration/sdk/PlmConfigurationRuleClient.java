package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/configurationRule/reviseRule")
    ResultInfo<ReviseRuleRespDto> reviseRule(ReviseRuleCmd cmd);


    /**
     * 删除 对当前Rev的Rule条目进行工程端的失效处理
     * @param removeRuleCmd
     * @return
     */
    @PostMapping("/remove")
    ResultInfo<Boolean> remove(RemoveRuleCmd removeRuleCmd);

}
