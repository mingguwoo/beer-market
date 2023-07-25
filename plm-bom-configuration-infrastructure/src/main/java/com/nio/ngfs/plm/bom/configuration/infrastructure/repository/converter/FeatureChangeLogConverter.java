package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.FeatureChangeLogMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Component
public class FeatureChangeLogConverter implements MapStructDataConverter<FeatureChangeLogAggr, BomsFeatureChangeLogEntity> {

    @Override
    public MapstructMapper<FeatureChangeLogAggr, BomsFeatureChangeLogEntity> getMapper() {
        return FeatureChangeLogMapper.INSTANCE;
    }

}
