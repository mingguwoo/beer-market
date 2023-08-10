package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Data
public class AddPcCmd implements Cmd {

    @NotBlank(message = "Model is blank")
    private String model;

    @NotBlank(message = "Model Year is blank")
    private String modelYear;

    @NotBlank(message = "Name is blank")
    @Size(max = 128, message = "Name max length is 128")
    private String name;

    private String basedOnBaseVehicleId;

    private String basedOnPcId;

    @NotBlank(message = "Create User is blank")
    private String createUser;

}
