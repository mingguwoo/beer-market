package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.BaseVehicleMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/19
 */
@Component
public class BaseVehicleConverter implements MapStructDataConverter<BaseVehicleAggr, BomsBasicVehicleEntity> {

    @Override
    public MapstructMapper<BaseVehicleAggr, BomsBasicVehicleEntity> getMapper() {
        return BaseVehicleMapper.INSTANCE;
    }


    @Override
    public void convertDoToEntityCallback(BaseVehicleAggr domainObject, BomsBasicVehicleEntity entity) {
        MapStructDataConverter.super.convertDoToEntityCallback(domainObject, entity);
    }

    @Override
    public void convertEntityToDoCallback(BomsBasicVehicleEntity entity, BaseVehicleAggr domainObject) {
        MapStructDataConverter.super.convertEntityToDoCallback(entity, domainObject);
    }


}