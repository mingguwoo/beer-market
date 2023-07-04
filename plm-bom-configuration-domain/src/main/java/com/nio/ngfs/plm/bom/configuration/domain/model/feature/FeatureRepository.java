package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureRepository extends Repository<FeatureAggr, FeatureId> {

    /**
     * 根据主键id查找
     *
     * @param id id
     * @return FeatureAggr
     */
    FeatureAggr getById(Long id);

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
