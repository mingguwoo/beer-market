package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author bill.wang
 * @date 2023/7/12
 */
@Data
public class AddOptionCmd implements Cmd {

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

    @NotBlank(message = "Feature Code is blank")
    @Size(max = 128, message = "Feature Code max length is 128")
    private String featureCode;

    @NotBlank(message = "Create User is blank")
    private String createUser;


}
