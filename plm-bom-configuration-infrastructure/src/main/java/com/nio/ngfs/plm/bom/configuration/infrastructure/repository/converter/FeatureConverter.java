package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.common.util.ConverterUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureConverter {

    public static BomsFeatureLibraryEntity convertDoToEntity(FeatureAggr aggr) {
        return BeanConvertUtils.convertTo(aggr, BomsFeatureLibraryEntity::new);
    }

    public static FeatureAggr convertEntityToDo(BomsFeatureLibraryEntity entity) {
        return BeanConvertUtils.convertTo(entity, FeatureAggr::new);
    }

    public static List<BomsFeatureLibraryEntity> convertDoListToEntityList(List<FeatureAggr> aggrList) {
        return ConverterUtil.convertList(aggrList, FeatureConverter::convertDoToEntity);
    }

    public static List<FeatureAggr> convertEntityListToDoList(List<BomsFeatureLibraryEntity> entityList) {
        return ConverterUtil.convertList(entityList, FeatureConverter::convertEntityToDo);
    }

}
