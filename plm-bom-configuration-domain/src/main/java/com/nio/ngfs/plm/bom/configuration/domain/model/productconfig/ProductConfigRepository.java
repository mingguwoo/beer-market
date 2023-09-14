package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ProductConfigRepository extends Repository<ProductConfigAggr, String> {

    /**
     * 根据车型和Model Year查询最新的PC
     *
     * @param modelCode 车型
     * @param modelYear Model Year
     * @return ProductConfigAggr
     */
    ProductConfigAggr queryLastPcByModelAndModelYear(String modelCode, String modelYear);

    /**
     * 根据Name查询
     *
     * @param name Name
     * @return ProductConfigAggr
     */
    ProductConfigAggr getByName(String name);

    /**
     * 根据pcId列表查询
     *
     * @param pcIdList      pcId列表
     * @param includeDelete 是否包含删除
     * @return ProductConfigAggr列表
     */
    List<ProductConfigAggr> queryByPcIdList(List<String> pcIdList, boolean includeDelete);

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<ProductConfigAggr> aggrList);

}
