package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;


import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextFeatureRepository extends Repository<ProductContextFeatureAggr,Long> {

    /**
     *  批量保存或更新
     * @param productContextFeatureAggrList
     */
    void saveOrUpdateBatch(List<ProductContextFeatureAggr> productContextFeatureAggrList);

    /**
     * 根据model code查询已有行
     * @param modelCode
     * @return
     */
    List<ProductContextFeatureAggr> queryByModelCode(String modelCode);
}
