package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Mapper
public interface ProductConfigOptionMapper extends MapstructMapper<ProductConfigOptionAggr, BomsProductConfigOptionEntity> {

    ProductConfigOptionMapper INSTANCE = Mappers.getMapper(ProductConfigOptionMapper.class);

}
