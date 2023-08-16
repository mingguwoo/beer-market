package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public class ProductContextFeatureFactory {

    public static List<ProductContextFeatureAggr> createProductContextFeatureList(List<OxoRowsQry> featureList, Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap, OxoListQry oxoListQry){
        List<ProductContextFeatureAggr> aggrs = new ArrayList<>();
        featureList.forEach(feature->{
            ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
            productContextFeatureAggr.setModelCode(oxoListQry.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode());
            productContextFeatureAggr.setFeatureCode(feature.getFeatureCode());
            productContextFeatureAggr.setFeatureGroup(feature.getGroup());
            productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
            aggrs.add(productContextFeatureAggr);
            featureOptionMap.get(feature).forEach(option->{
                    ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
                    aggr.setModelCode(oxoListQry.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode());
                    aggr.setFeatureCode(option.getFeatureCode());
                    aggr.setFeatureGroup(option.getGroup());
                    aggr.setType(ProductContextFeatureEnum.OPTION.getType());
                    aggrs.add(aggr);
            });
        });
        return aggrs;
    }
}
