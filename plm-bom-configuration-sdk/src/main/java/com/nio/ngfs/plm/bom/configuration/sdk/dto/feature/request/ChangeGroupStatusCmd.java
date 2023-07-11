package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@Data
public class ChangeGroupStatusCmd implements Cmd {

    @NotNull(message = "Group Id is null")
    private Long groupId;

    @NotBlank(message = "Status is blank")
    private String status;

}
