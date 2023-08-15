package com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/26
 */
@Data
public class ChangeBaseVehicleStatusCmd implements Cmd {

    @NotBlank(message = "Base Vehicle Id is blank")
    private String baseVehicleId;

    @NotBlank(message = "Status is blank")
    private String status;
}
