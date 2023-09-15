package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
public class AddOptionCmd implements Cmd {

    @NotBlank(message = "Code is blank")
    private String code;

    @NotNull(message = "Parent Id is null")
    private Long parentId;

    @NotBlank(message = "Display Name is blank")
    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 1024, message = "Remark max length is 128")
    private String remark;

    @NotBlank(message = "Create User is blank")
    private String createUser;

    private boolean confirm;

}
