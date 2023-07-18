package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author bill.wang
 * @date 2023/7/18
 */

@Mapper
public interface BaseVehicleMapper extends MapstructMapper<BaseVehicleAggr, BomsBasicVehicleEntity> {

    BaseVehicleMapper INSTANCE = Mappers.getMapper(BaseVehicleMapper.class);

}
