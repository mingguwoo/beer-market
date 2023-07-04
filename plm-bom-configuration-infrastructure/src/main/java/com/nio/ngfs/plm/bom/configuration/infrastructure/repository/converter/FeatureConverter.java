package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
public class FeatureConverter implements DataConverter<FeatureAggr, BomsFeatureLibraryEntity> {

    @Override
    public FeatureAggr newDo() {
        return new FeatureAggr();
    }

    @Override
    public BomsFeatureLibraryEntity newEntity() {
        return new BomsFeatureLibraryEntity();
    }

}
