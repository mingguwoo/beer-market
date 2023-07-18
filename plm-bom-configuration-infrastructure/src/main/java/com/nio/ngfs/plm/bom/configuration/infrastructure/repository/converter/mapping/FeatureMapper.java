package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
@Mapper
public interface FeatureMapper extends MapstructMapper<FeatureAggr, BomsFeatureLibraryEntity> {

    FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

}
