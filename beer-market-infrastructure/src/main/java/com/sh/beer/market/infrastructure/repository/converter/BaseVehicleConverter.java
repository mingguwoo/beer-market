package com.sh.beer.market.infrastructure.repository.converter;


import com.sh.beer.market.domain.model.basevehicle.BaseVehicleAggr;
import com.sh.beer.market.infrastructure.repository.converter.common.MapStructDataConverter;
import com.sh.beer.market.infrastructure.repository.entity.BomsBaseVehicleEntity;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/19
 */
@Component
public class BaseVehicleConverter implements MapStructDataConverter<BaseVehicleAggr, BomsBaseVehicleEntity> {

   /* @Override
    public MapstructMapper<BaseVehicleAggr, BomsBaseVehicleEntity> getMapper() {
        return BaseVehicleMapper.INSTANCE;
    }
*/
}
