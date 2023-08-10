package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Mapper
public interface ProductConfigMapper extends MapstructMapper<ProductConfigAggr, BomsProductConfigEntity> {

    ProductConfigMapper INSTANCE = Mappers.getMapper(ProductConfigMapper.class);

}
