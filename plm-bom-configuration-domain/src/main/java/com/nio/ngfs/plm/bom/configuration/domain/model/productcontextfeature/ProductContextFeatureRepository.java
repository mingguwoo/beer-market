package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;


import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextFeatureRepository extends Repository<ProductContextFeatureAggr,Long> {

    public void batchSave(List<ProductContextFeatureAggr> productContextFeatureAggrList);

    public List<ProductContextFeatureAggr> queryByModelCode(String modelCode);
}
