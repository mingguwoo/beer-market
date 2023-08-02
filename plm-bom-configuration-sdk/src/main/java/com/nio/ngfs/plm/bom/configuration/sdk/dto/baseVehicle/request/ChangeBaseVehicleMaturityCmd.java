package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/31
 */
@Data
public class ChangeBaseVehicleMaturityCmd implements Cmd {

    @NotBlank(message = "Model Code is blank")
    private String modelCode;

    @NotBlank(message = "Model Year is blank")
    private String modelYear;

    @NotBlank(message = "Base Vehicle Id is blank")
    private String baseVehicleId;

    @NotBlank(message = "Maturity is blank")
    private String maturity;
}