package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * @author bill.wang
 * @date 2023/7/13
 */
@Data
public class ChangeOptionStatusCmd implements Cmd {

    @NotBlank(message = "Option Code is blank")
    @Size(max = 128, message = "Option Code max length is 128")
    private String optionCode;

    @NotBlank(message = "Status is blank")
    private String status;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}
