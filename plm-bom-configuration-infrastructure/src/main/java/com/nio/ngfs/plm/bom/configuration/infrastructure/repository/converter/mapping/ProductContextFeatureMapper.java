package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Mapper
public interface ProductContextFeatureMapper extends MapstructMapper<ProductContextFeatureAggr, BomsProductContextFeatureEntity> {
    ProductContextFeatureMapper INSTANCE = Mappers.getMapper(ProductContextFeatureMapper.class);
}
