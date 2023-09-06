package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearId;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ModelYearConfigMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelYearEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Component
public class ProductConfigModelYearConverter implements MapStructDataConverter<ProductConfigModelYearAggr, BomsProductConfigModelYearEntity> {

    @Override
    public MapstructMapper<ProductConfigModelYearAggr, BomsProductConfigModelYearEntity> getMapper() {
        return ModelYearConfigMapper.INSTANCE;
    }

    @Override
    public void convertDoToEntityCallback(ProductConfigModelYearAggr domainObject, BomsProductConfigModelYearEntity entity) {
        entity.setModel(domainObject.getProductConfigModelYearId().getModel());
        entity.setModelYear(domainObject.getProductConfigModelYearId().getModelYear());
    }

    @Override
    public void convertEntityToDoCallback(BomsProductConfigModelYearEntity entity, ProductConfigModelYearAggr domainObject) {
        domainObject.setProductConfigModelYearId(new ProductConfigModelYearId(entity.getModel(), entity.getModelYear()));
    }

}
