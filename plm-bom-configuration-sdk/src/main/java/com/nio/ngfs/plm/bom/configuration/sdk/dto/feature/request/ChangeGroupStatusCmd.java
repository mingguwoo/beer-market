package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@Data
public class ChangeGroupStatusCmd implements Cmd {

    @NotBlank(message = "Group Code is blank")
    @Size(max = 128, message = "Group Code max length is 128")
    private String groupCode;

    @NotBlank(message = "Status is blank")
    private String status;

}
