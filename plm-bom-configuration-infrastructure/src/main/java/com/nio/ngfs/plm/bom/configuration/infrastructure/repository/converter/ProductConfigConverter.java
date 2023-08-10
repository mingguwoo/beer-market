package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ProductConfigMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Component
public class ProductConfigConverter implements MapStructDataConverter<ProductConfigAggr, BomsProductConfigEntity> {

    @Override
    public MapstructMapper<ProductConfigAggr, BomsProductConfigEntity> getMapper() {
        return ProductConfigMapper.INSTANCE;
    }

}
