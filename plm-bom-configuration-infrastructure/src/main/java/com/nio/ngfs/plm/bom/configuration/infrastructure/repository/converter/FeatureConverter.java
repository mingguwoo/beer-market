package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.common.util.ConverterUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.po.BomsFeatureLibraryPO;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureConverter {

    public static BomsFeatureLibraryPO convertEntityToPo(FeatureAggr entity) {
        return BeanConvertUtils.convertTo(entity, BomsFeatureLibraryPO::new);
    }

    public static FeatureAggr convertPoToEntity(BomsFeatureLibraryPO po) {
        return BeanConvertUtils.convertTo(po, FeatureAggr::new);
    }

    public static List<BomsFeatureLibraryPO> convertEntityListToPoList(List<FeatureAggr> entityList) {
        return ConverterUtil.convertList(entityList, FeatureConverter::convertEntityToPo);
    }

    public static List<FeatureAggr> convertPoListToEntityList(List<BomsFeatureLibraryPO> poList) {
        return ConverterUtil.convertList(poList, FeatureConverter::convertPoToEntity);
    }

}
