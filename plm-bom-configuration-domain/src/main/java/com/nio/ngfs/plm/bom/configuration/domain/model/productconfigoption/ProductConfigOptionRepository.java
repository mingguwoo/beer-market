package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/11
 */
public interface ProductConfigOptionRepository extends Repository<ProductConfigOptionAggr, ProductConfigOptionId> {

    /**
     * 根据pcId查询
     *
     * @param pcId PC Id
     * @return ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> queryByPcId(Long pcId);

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<ProductConfigOptionAggr> aggrList);

    /**
     * 根据PC Id列表查询
     *
     * @param pcIdList PC Id列表
     * @return 聚合根列表
     */
    List<ProductConfigOptionAggr> queryByPcIdList(List<Long> pcIdList);

}
