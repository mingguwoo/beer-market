package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.OxoVersionSnapshotMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Component
public class OxoVersionSnapshotConverter implements MapStructDataConverter<OxoVersionSnapshotAggr, BomsOxoVersionSnapshotEntity> {

    @Override
    public MapstructMapper<OxoVersionSnapshotAggr, BomsOxoVersionSnapshotEntity> getMapper() {
        return OxoVersionSnapshotMapper.INSTANCE;
    }

}
