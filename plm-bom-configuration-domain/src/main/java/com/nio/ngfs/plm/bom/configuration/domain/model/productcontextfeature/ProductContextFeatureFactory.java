package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public class ProductContextFeatureFactory {

    /**
     * 初始化product context时新建product context行
     * @param ProductContextFeatureList
     * @param featureList
     * @param featureOptionMap
     * @param modelCode
     * @return
     */
    public static void createProductContextFeatureList(List<ProductContextFeatureAggr> ProductContextFeatureList,List<OxoRowsQry> featureList, Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap, String modelCode){
        Set<ProductContextFeatureAggr> existFeatureSet = new HashSet<>();
        //先记录已有行
        if (Objects.nonNull(ProductContextFeatureList)){
            ProductContextFeatureList.forEach(aggr->{
                existFeatureSet.add(aggr);
            });
        }
        featureList.forEach(feature->{
            //如果不是ad00下的且不存在
            if (!Objects.equals(feature.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
                productContextFeatureAggr.setModelCode(modelCode);
                productContextFeatureAggr.setFeatureCode(feature.getFeatureCode());
                productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
                if (!existFeatureSet.contains(productContextFeatureAggr)){
                    ProductContextFeatureList.add(productContextFeatureAggr);
                    featureOptionMap.get(feature).forEach(option->{
                        ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
                        aggr.setModelCode(modelCode);
                        aggr.setFeatureCode(option.getFeatureCode());
                        aggr.setType(ProductContextFeatureEnum.OPTION.getType());
                        if (!existFeatureSet.contains(aggr)){
                            ProductContextFeatureList.add(aggr);
                        }
                    });
                }

            }
        });
    }

    /**
     * 初始化product context时新建product context的model year相关行
     * @param productContextFeatureAggrList
     * @param featureModelYearAggr
     * @param modelCode
     * @param modelYearMap
     */
    public static void createModelYearProductContextFeature(List<ProductContextFeatureAggr> productContextFeatureAggrList, FeatureAggr featureModelYearAggr,String modelCode,Map<String,String> modelYearMap){
        List<FeatureAggr> optionAggrList = featureModelYearAggr.getChildrenList();
        Set<ProductContextFeatureAggr> existModelYearSet = new HashSet<>();
        //记录原有所有modelYear相关行
        if (Objects.nonNull(productContextFeatureAggrList)){
            productContextFeatureAggrList.forEach(row->{
                if (Objects.equals(row.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO), ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                    existModelYearSet.add(row);
                }
            });
        }
        //先添加AF00
        ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
        productContextFeatureAggr.setModelCode(modelCode);
        productContextFeatureAggr.setFeatureCode(featureModelYearAggr.getFeatureCode());
        productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
        if (!existModelYearSet.contains(productContextFeatureAggr)){
            productContextFeatureAggrList.add(productContextFeatureAggr);
        }
        //再添加AF00下面的option
        optionAggrList.forEach(option->{
            ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
            aggr.setModelCode(modelCode);
            aggr.setFeatureCode(option.getFeatureCode());
            aggr.setType(ProductContextFeatureEnum.OPTION.getType());
            if (!existModelYearSet.contains(productContextFeatureAggr)) {
                productContextFeatureAggrList.add(aggr);
            }
            //记录modelYear的displayname与optionCode的对应关系，用于后续打勾
            modelYearMap.put(option.getDisplayName(),option.getFeatureCode());
        });
    }


}
