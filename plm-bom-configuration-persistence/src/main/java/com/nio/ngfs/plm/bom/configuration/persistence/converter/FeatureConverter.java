package com.nio.ngfs.plm.bom.configuration.persistence.converter;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.persistence.entity.BomsFeatureLibraryEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureConverter {

    public static BomsFeatureLibraryEntity convertToEntity(FeatureAggr featureAggr) {
        return BeanConvertUtils.convertTo(featureAggr, BomsFeatureLibraryEntity::new);
    }

    public static FeatureAggr convertEntityTo(BomsFeatureLibraryEntity featureEntity) {
        return BeanConvertUtils.convertTo(featureEntity, FeatureAggr::new);
    }

}
