package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xiaozhou.tu
 * @date 2023/10/23
 */
@Data
public class DeleteRuleCmd implements Cmd {

    @NotNull(message = "Group Id is null")
    private Long groupId;

    @NotNull(message = "Rule Id is null")
    private Long ruleId;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}