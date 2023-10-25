package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author wangchao.wang
 */

@Data
public class RemoveRuleCmd implements Cmd {


    @NotEmpty(message = "ruleIds不能为空")
    private List<Long> ruleIds;


    private String userName;


}
