package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Data
public class GetBaseVehicleOptionsQry implements Qry {

    private String modelCode;

    private String modelYear;
}
