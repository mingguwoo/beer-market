package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface ProductConfigModelYearRepository extends Repository<ProductConfigModelYearAggr, ProductConfigModelYearId> {

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<ProductConfigModelYearAggr> aggrList);

    /**
     * 根据车型查询
     *
     * @param model 车型
     * @return ProductConfigModelYearAggr列表
     */
    List<ProductConfigModelYearAggr> queryByModel(String model);

}
