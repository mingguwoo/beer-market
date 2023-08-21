package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionId;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ProductConfigModelOptionMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Component
public class ProductConfigModelOptionConverter implements MapStructDataConverter<ProductConfigModelOptionAggr, BomsProductConfigModelOptionEntity> {

    @Override
    public MapstructMapper<ProductConfigModelOptionAggr, BomsProductConfigModelOptionEntity> getMapper() {
        return ProductConfigModelOptionMapper.INSTANCE;
    }

    @Override
    public void convertDoToEntityCallback(ProductConfigModelOptionAggr domainObject, BomsProductConfigModelOptionEntity entity) {
        entity.setModelCode(domainObject.getModelCode());
        entity.setOptionCode(domainObject.getOptionCode());
    }

    @Override
    public void convertEntityToDoCallback(BomsProductConfigModelOptionEntity entity, ProductConfigModelOptionAggr domainObject) {
        domainObject.setProductConfigModelOptionId(new ProductConfigModelOptionId(entity.getModelCode(), entity.getOptionCode()));
    }

}
