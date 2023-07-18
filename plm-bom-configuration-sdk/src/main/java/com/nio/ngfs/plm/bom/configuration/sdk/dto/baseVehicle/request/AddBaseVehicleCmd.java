package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Data
public class AddBaseVehicleCmd implements Cmd {

    @NotBlank(message = "Model is blank")
    private String model;

    @NotBlank(message = "Model Year is blank")
    private String modelYear;

    @NotBlank(message = "Region is blank")
    private String region;

    @NotBlank(message = "Drive hand is blank")
    private String driveHand;

    @NotBlank(message = "Sales Version is blank")
    private String salesVersion;

    private String maturity;

    private boolean copyFrom;

    private String regionEn;

    private String regionCn;

    private String driveHandEn;

    private String driveHandCn;

    private String salesVersionEn;

    private String salesVersionCn;

    @NotBlank(message = "Create User is blank")
    private String createUser;

}
