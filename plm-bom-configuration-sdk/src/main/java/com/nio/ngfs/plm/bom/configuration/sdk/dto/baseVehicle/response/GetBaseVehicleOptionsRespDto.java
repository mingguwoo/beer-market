package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Data
public class GetBaseVehicleOptionsRespDto implements Dto {

    private List<String> regionOptionCode;

    private List<String> driveHand;

    private List<String> salesVersion;
}

