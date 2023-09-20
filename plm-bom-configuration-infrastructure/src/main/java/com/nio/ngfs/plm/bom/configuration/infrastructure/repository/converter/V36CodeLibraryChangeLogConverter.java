package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog.V36CodeLibraryChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.V36CodeLibraryChangeLogMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Component
public class V36CodeLibraryChangeLogConverter implements MapStructDataConverter<V36CodeLibraryChangeLogAggr, BomsV36CodeLibraryChangeLogEntity> {
    @Override
    public MapstructMapper<V36CodeLibraryChangeLogAggr, BomsV36CodeLibraryChangeLogEntity> getMapper() {
        return V36CodeLibraryChangeLogMapper.INSTANCE;
    }
}
