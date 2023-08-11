package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionId;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ProductConfigOptionMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
@Component
public class ProductConfigOptionConverter implements MapStructDataConverter<ProductConfigOptionAggr, BomsProductConfigOptionEntity> {

    @Override
    public MapstructMapper<ProductConfigOptionAggr, BomsProductConfigOptionEntity> getMapper() {
        return ProductConfigOptionMapper.INSTANCE;
    }

    @Override
    public void convertDoToEntityCallback(ProductConfigOptionAggr domainObject, BomsProductConfigOptionEntity entity) {
        entity.setPcId(domainObject.getPcId());
        entity.setOptionCode(domainObject.getOptionCode());
    }

    @Override
    public void convertEntityToDoCallback(BomsProductConfigOptionEntity entity, ProductConfigOptionAggr domainObject) {
        domainObject.setProductConfigOptionId(new ProductConfigOptionId(entity.getPcId(), entity.getOptionCode()));
    }

}
