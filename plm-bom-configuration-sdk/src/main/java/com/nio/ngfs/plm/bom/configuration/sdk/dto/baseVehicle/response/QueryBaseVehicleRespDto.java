package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/25
 */
@Data
public class QueryBaseVehicleRespDto implements Dto {

    private List<BaseVehicleRespDto> baseVehicleRespDtoList;

    private int count;
}
