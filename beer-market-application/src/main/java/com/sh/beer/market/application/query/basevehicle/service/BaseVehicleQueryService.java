package com.sh.beer.market.application.query.basevehicle.service;


import com.sh.beer.market.sdk.dto.basevehicle.response.BaseVehicleRespDto;

import java.util.List;

/**
 * @author
 * @date 2023/8/3
 */
public interface BaseVehicleQueryService {
    List<BaseVehicleRespDto> completeBaseVehicle(List<BaseVehicleRespDto> filteredDto);
}
