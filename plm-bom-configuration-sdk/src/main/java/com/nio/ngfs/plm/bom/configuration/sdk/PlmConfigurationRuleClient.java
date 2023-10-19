package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.GetPurposeOptionListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.AddRuleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetPurposeOptionListRespDto;
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
    @PostMapping("/configurationrule/addRule")
    ResultInfo<AddRuleRespDto> addRule(AddRuleCmd cmd);

    /**
     * 查询Purpose选项列表
     */
    @PostMapping("/configurationrule/getPurposeOptionList")
    ResultInfo<List<GetPurposeOptionListRespDto>> getPurposeOptionList(GetPurposeOptionListQry qry);

}
