package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import org.mapstruct.factory.Mappers;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
public interface ProductContextFeatureMapper extends MapstructMapper<ProductContextFeatureAggr, BomsProductContextFeatureEntity> {
    ProductContextFeatureMapper INSTANCE = Mappers.getMapper(ProductContextFeatureMapper.class);
}
