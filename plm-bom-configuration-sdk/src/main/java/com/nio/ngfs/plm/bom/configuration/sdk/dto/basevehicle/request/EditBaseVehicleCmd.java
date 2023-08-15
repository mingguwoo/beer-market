package com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/20
 */
@Data
public class EditBaseVehicleCmd implements Cmd {

    @NotBlank(message = "Base Vehicle Id is blank")
    private String baseVehicleId;

    @NotBlank(message = "Model Code is blank")
    private String modelCode;

    @NotBlank(message = "Model Year is blank")
    private String modelYear;

    @NotBlank(message = "Region Option Code is blank")
    private String regionOptionCode;

    @NotBlank(message = "Drive hand is blank")
    private String driveHand;

    @NotBlank(message = "Sales Version is blank")
    private String salesVersion;

    private String maturity;

    @NotBlank(message = "Update User is blank")
    private String updateUser;
}
