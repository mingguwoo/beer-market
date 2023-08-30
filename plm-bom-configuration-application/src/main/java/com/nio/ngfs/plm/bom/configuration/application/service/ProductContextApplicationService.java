package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextApplicationService {

    /**
     * 同步oxo，生成product context
     * @param oxoSnapShot
     */
    void addProductContext(String oxoSnapShot);

    /**
     * 将ProductContext数据存到数据库
     * @param productContextList
     * @param productContextFeatureList
     */
    void saveProductContextToDb(List<ProductContextAggr> productContextList, List<ProductContextFeatureAggr> productContextFeatureList);
}
