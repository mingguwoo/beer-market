package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ProductContextMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductContextMapper;

/**
 * @author bill.wang
 * @date 2023/8/15
 */
public class ProductContextConverter implements MapStructDataConverter<ProductContextAggr, BomsProductContextEntity> {
    @Override
    public MapstructMapper<ProductContextAggr, BomsProductContextEntity> getMapper() {
        return ProductContextMapper.INSTANCE;
    }
}
