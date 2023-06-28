package com.nio.ngfs.plm.bom.configuration.persistence.converter;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.persistence.entity.FeatureEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureConverter {

    public static FeatureEntity convertToEntity(FeatureAggr featureAggr) {
        return BeanConvertUtils.convertTo(featureAggr, FeatureEntity::new);
    }

    public static FeatureAggr convertEntityTo(FeatureEntity featureEntity) {
        return BeanConvertUtils.convertTo(featureEntity, FeatureAggr::new);
    }

}
