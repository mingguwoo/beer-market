package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureRepository {

    /**
     * 保存
     *
     * @param featureAggr featureAggr
     */
    void save(FeatureAggr featureAggr);

    /**
     * 查找Feature
     *
     * @param featureCode featureCode
     * @param featureType 类型
     * @return Feature
     */
    FeatureAggr find(String featureCode, FeatureTypeEnum featureType);

    /**
     * 查找Feature
     *
     * @param id id
     * @return Feature
     */
    FeatureAggr find(Long id);

    List<FeatureAggr> queryByParentFeatureCode(String parentFeatureCode);

    List<FeatureAggr> listFeatureLibrary();

}
