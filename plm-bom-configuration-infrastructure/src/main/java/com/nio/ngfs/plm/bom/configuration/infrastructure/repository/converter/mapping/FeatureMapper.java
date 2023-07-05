package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
@Mapper
public interface FeatureMapper {

    FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

    /**
     * convertDoToEntity
     *
     * @param domainObject domainObject
     * @return BomsFeatureLibraryEntity
     */
    @Mapping(source = "FeatureAggr", target = "BomsFeatureLibraryEntity")
    BomsFeatureLibraryEntity convertDoToEntity(FeatureAggr domainObject);

    /**
     * convertEntityToDo
     *
     * @param entity entity
     * @return FeatureAggr
     */
    @Mapping(source = "BomsFeatureLibraryEntity", target = "FeatureAggr")
    FeatureAggr convertEntityToDo(BomsFeatureLibraryEntity entity);

}
