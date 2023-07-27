package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.OxoFeatureOptionMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Component
public class OxoFeatureOptionConverter implements MapStructDataConverter<OxoFeatureOptionAggr, BomsOxoFeatureOptionEntity> {

    @Override
    public MapstructMapper<OxoFeatureOptionAggr, BomsOxoFeatureOptionEntity> getMapper() {
        return OxoFeatureOptionMapper.INSTANCE;
    }

}
