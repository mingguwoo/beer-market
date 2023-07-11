package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureRepository extends Repository<FeatureAggr, FeatureId> {

    /**
     * 根据id查找聚合根
     *
     * @param id id
     * @return 聚合根
     */
    FeatureAggr getById(Long id);

    /**
     * 根据parentFeatureCode和类型查找
     *
     * @param parentFeatureCode parentFeatureCode
     * @param type              类型
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryByParentFeatureCodeAndType(String parentFeatureCode, String type);

    /**
     * 查找所有
     *
     * @return FeatureAggr列表
     */
    List<FeatureAggr> queryAll();

}
