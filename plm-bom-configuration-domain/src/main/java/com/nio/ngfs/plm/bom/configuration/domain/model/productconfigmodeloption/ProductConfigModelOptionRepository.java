package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
public interface ProductConfigModelOptionRepository extends Repository<ProductConfigModelOptionAggr, ProductConfigModelOptionId> {

    /**
     * 根据车型查询
     *
     * @param modelCode 车型
     * @return ProductConfigModelOptionAggr列表
     */
    List<ProductConfigModelOptionAggr> queryByModel(String modelCode);

    /**
     * 批量保存
     *
     * @param aggrList 列表
     */
    void batchSave(List<ProductConfigModelOptionAggr> aggrList);

}
