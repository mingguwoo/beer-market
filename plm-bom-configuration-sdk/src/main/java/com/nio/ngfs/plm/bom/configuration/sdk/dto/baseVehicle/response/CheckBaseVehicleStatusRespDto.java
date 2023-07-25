package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/7/21
 */
@Data
public class CheckBaseVehicleStatusRespDto implements Dto {

    private boolean isReleased;
}
