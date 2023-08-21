package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Data
public class GetBasedOnBaseVehicleListQry implements Qry {

    @NotBlank(message = "Model is blank")
    private String model;

}
