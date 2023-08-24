package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ProductContextFeatureMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Component
public class ProductContextFeatureConverter implements MapStructDataConverter<ProductContextFeatureAggr, BomsProductContextFeatureEntity>{
    @Override
    public MapstructMapper<ProductContextFeatureAggr, BomsProductContextFeatureEntity> getMapper() {
        return ProductContextFeatureMapper.INSTANCE;
    }
}

