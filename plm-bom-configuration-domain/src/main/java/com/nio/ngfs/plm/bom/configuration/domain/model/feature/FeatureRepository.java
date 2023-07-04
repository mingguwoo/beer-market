package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureRepository extends Repository<FeatureAggr, Long> {

    /**
     * 根据featureCode和类型查找
     *
     * @param featureCode Feature编码
     * @param type        类型
     * @return FeatureAggr
     */
    FeatureAggr getByFeatureCodeAndType(String featureCode, String type);

    /**
     * 根据parentFeatureCode查找
     *
     * @param parentFeatureCode parentFeatureCode
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode);

    /**
     * 查找所有
     *
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryAll();

}
