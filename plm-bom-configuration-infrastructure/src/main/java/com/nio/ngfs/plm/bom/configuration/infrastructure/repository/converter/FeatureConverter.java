package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.FeatureMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
public class FeatureConverter implements MapStructDataConverter<FeatureAggr, BomsFeatureLibraryEntity> {

    @Override
    public BomsFeatureLibraryEntity doToEntity(FeatureAggr domainObject) {
        return FeatureMapper.INSTANCE.convertDoToEntity(domainObject);
    }

    @Override
    public FeatureAggr entityToDo(BomsFeatureLibraryEntity entity) {
        return FeatureMapper.INSTANCE.convertEntityToDo(entity);
    }

}
