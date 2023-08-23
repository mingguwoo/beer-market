package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;


import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextRepository extends Repository<ProductContextAggr,Long> {

    /**
     * modelCode获取Product Context数据
     * @param modelCode
     * @return
     */
    List<ProductContextAggr> queryByModelCode(String modelCode);

    /**
     * 批量保存或更新
     * @param productContextAggrs
     */
    void saveOrUpdateBatch(List<ProductContextAggr> productContextAggrs);

}
