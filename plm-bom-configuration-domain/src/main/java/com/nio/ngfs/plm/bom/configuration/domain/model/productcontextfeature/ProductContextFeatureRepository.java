package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;


import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextFeatureRepository extends Repository<ProductContextFeatureAggr,Long> {

    /**
     *  批量保存
     * @param productContextFeatureAggrList
     */
    void saveBatch(List<ProductContextFeatureAggr> productContextFeatureAggrList);

    /**
     * 根据model code查询已有行
     * @param modelCode
     * @return
     */
    List<ProductContextFeatureAggr> queryByModelCode(String modelCode);

    /**
     * 根据modelCode和Feature Code列表获取Product Context Feature数据
     *
     * @param modelCode modelCode
     * @param featureCodeList featureCodeList
     * @return ProductContextFeatureAggr列表
     */
    List<ProductContextFeatureAggr> queryByModelCodeAndFeatureCode(String modelCode, List<String> featureCodeList);

    /**
     * 获取下发用全量数据
     * @return
     */
    List<ProductContextFeatureAggr> queryAll();
}
