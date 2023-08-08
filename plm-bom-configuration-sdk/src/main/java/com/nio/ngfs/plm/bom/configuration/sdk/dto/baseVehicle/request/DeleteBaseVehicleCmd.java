package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/21
 */
@Data
public class DeleteBaseVehicleCmd implements Cmd {

    @NotBlank(message = "Base Vehicle Id is blank")
    private String baseVehicleId;

    @NotBlank(message = "Model Code is blank")
    private String modelCode;
}
