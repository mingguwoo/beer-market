package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class AddGroupCmd implements Cmd {

    @NotBlank(message = "Group Code is blank")
    @Size(max = 128, message = "Group Code max length is 128")
    private String groupCode;

    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Description Name max length is 128")
    private String description;

    @NotBlank(message = "Brand is blank")
    private String brand;

    @NotBlank(message = "Create User is blank")
    private String createUser;

}
