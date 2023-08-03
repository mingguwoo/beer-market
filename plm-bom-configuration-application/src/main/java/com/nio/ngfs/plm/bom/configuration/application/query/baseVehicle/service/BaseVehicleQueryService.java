package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.service;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleRespDto;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
public interface BaseVehicleQueryService {
    List<BaseVehicleRespDto> completeBaseVehicle(List<BaseVehicleRespDto> filteredDto);
}
