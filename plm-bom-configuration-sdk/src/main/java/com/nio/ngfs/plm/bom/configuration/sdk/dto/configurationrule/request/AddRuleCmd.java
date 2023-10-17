package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
public class AddRuleCmd implements Cmd {

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotNull
    private Integer purpose;

    @Size(max = 1024, message = "Description max length is 1024")
    private String description;

}
