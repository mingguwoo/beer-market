package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
@Data
public class EditGroupCmd implements Cmd {

    @NotNull(message = "Group Id is null")
    private Long groupId;

    @NotBlank(message = "Group Code is blank")
    @Size(max = 128, message = "Group Code max length is 128")
    private String groupCode;

    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Description Name max length is 128")
    private String description;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}
