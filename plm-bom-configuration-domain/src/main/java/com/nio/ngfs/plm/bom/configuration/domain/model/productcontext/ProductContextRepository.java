package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;


import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextRepository extends Repository<ProductContextAggr, Long> {

    /**
     * modelCode获取Product Context数据
     *
     * @param modelCode
     * @return
     */
    List<ProductContextAggr> queryByModelCode(String modelCode);

    /**
     * 批量保存
     *
     * @param productContextAggrList
     */
    void addOrUpdateBatch(List<ProductContextAggr> productContextAggrList);

    /**
     * 批量删除
     *
     * @param productContextAggrList
     */
    void removeBatchByIds(List<ProductContextAggr> productContextAggrList);

    /**
     * 根据modelCode和modelYear列表查询
     *
     * @param modelCode     modelCode
     * @param modelYearList modelYear列表
     * @return ProductContextAggr列表
     */
    List<ProductContextAggr> queryByModelAndModelYearList(String modelCode, List<String> modelYearList);

    /**
     * 获取下发用全量数据
     * @return
     */
    List<ProductContextAggr> queryAll();
}
