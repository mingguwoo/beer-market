package com.sh.beer.market.infrastructure.repository.converter.mapping;


import com.sh.beer.market.domain.model.basevehicle.BaseVehicleAggr;
import com.sh.beer.market.infrastructure.repository.entity.BomsBaseVehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author
 * @date 2023/7/18
 */

@Mapper
public interface BaseVehicleMapper extends MapstructMapper<BaseVehicleAggr, BomsBaseVehicleEntity> {

    BaseVehicleMapper INSTANCE = Mappers.getMapper(BaseVehicleMapper.class);

}
