package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public interface ProductContextApplicationService {

    /**
     * 同步oxo，生成product context
     * @param oxoListQry
     * @param owner
     */
    void addProductContext(OxoListRespDto oxoListQry, String owner);

    /**
     * 更新Product Context打勾数据
     * @param productContextList
     * @param productContextFeatureList
     */
    void saveProductContextToDb(List<ProductContextAggr> productContextList, List<ProductContextFeatureAggr> productContextFeatureList,List<ProductContextAggr> removeProductContextAggrList);
}
