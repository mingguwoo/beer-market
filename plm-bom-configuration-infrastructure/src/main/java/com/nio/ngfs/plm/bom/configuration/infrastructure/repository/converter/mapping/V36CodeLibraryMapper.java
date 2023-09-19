package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Mapper
public interface V36CodeLibraryMapper extends MapstructMapper<V36CodeLibraryAggr, BomsV36CodeLibraryEntity> {

    V36CodeLibraryMapper INSTANCE = Mappers.getMapper(V36CodeLibraryMapper.class);

}
