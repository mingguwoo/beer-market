package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Mapper
public interface V36CodeLibraryChangeLogMapper extends MapstructMapper<V36CodeLibraryChangeLogAggr, BomsV36CodeLibraryChangeLogEntity>{
    V36CodeLibraryChangeLogMapper INSTANCE = Mappers.getMapper(V36CodeLibraryChangeLogMapper.class);
}
