package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig.ModelYearConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig.ModelYearConfigId;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.MapStructDataConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.ModelYearConfigMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Component
public class ModelYearConfigConverter implements MapStructDataConverter<ModelYearConfigAggr, BomsModelYearConfigEntity> {

    @Override
    public MapstructMapper<ModelYearConfigAggr, BomsModelYearConfigEntity> getMapper() {
        return ModelYearConfigMapper.INSTANCE;
    }

    @Override
    public void convertDoToEntityCallback(ModelYearConfigAggr domainObject, BomsModelYearConfigEntity entity) {
        entity.setModel(domainObject.getModelYearConfigId().getModel());
        entity.setModelYear(domainObject.getModelYearConfigId().getModelYear());
    }

    @Override
    public void convertEntityToDoCallback(BomsModelYearConfigEntity entity, ModelYearConfigAggr domainObject) {
        domainObject.setModelYearConfigId(new ModelYearConfigId(entity.getModel(), entity.getModelYear()));
    }

}
