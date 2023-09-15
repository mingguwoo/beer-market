package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.V36CodeLibraryMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
public class V36CodeLibraryConverter implements MapStructDataConverter<V36CodeLibraryAggr, BomsV36CodeLibraryEntity> {

    @Override
    public MapstructMapper<V36CodeLibraryAggr, BomsV36CodeLibraryEntity> getMapper() {
        return V36CodeLibraryMapper.INSTANCE;
    }

}
