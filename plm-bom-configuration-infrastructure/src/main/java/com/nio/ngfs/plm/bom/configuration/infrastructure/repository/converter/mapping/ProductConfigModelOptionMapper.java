package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Mapper
public interface ProductConfigModelOptionMapper extends MapstructMapper<ProductConfigModelOptionAggr, BomsProductConfigModelOptionEntity> {

    ProductConfigModelOptionMapper INSTANCE = Mappers.getMapper(ProductConfigModelOptionMapper.class);

}
