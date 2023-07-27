package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.OxoOptionPackageMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Component
public class OxoOptionPackageConverter implements MapStructDataConverter<OxoOptionPackageAggr, BomsOxoOptionPackageEntity> {

    @Override
    public MapstructMapper<OxoOptionPackageAggr, BomsOxoOptionPackageEntity> getMapper() {
        return OxoOptionPackageMapper.INSTANCE;
    }

}
