package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
@Mapper
public interface ProductContextMapper extends MapstructMapper<ProductContextAggr, BomsProductContextEntity>{
    ProductContextMapper INSTANCE = Mappers.getMapper(ProductContextMapper.class);
}
