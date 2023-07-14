package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author bill.wang
 * @date 2023/7/13
 */
@Data
public class EditOptionCmd implements Cmd {

    @NotBlank(message = "Option Code is blank")
    @Size(max = 128, message = "Option Code max length is 128")
    private String optionCode;

    @NotBlank(message = "Display Name is blank")
    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Description max length is 128")
    private String description;

    @NotBlank(message = "Requestor is blank")
    private String requestor;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}
