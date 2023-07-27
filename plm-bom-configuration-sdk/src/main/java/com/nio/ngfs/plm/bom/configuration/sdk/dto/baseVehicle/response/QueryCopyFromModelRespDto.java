package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Data
public class QueryCopyFromModelRespDto implements Dto {

    List<BaseVehicleRespDto> baseVehicleList;
}
