package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Mapper
public interface OxoFeatureOptionMapper extends MapstructMapper<OxoFeatureOptionAggr, BomsOxoFeatureOptionEntity> {

    OxoFeatureOptionMapper INSTANCE = Mappers.getMapper(OxoFeatureOptionMapper.class);

}
