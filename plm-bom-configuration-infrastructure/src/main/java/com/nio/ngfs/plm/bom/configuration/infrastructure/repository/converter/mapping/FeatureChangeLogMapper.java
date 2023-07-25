package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Mapper
public interface FeatureChangeLogMapper extends MapstructMapper<FeatureChangeLogAggr, BomsFeatureChangeLogEntity> {

    FeatureChangeLogMapper INSTANCE = Mappers.getMapper(FeatureChangeLogMapper.class);

}
