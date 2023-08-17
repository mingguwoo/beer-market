package com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public class ProductContextFeatureFactory {

    /**
     * 初始化product context时新建product context行
     * @param featureList
     * @param featureOptionMap
     * @param modelCode
     * @param featureGroupMap
     * @return
     */
    public static List<ProductContextFeatureAggr> createProductContextFeatureList(List<OxoRowsQry> featureList, Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap, String modelCode,Map<String,Long> featureGroupMap){
        List<ProductContextFeatureAggr> aggrs = new ArrayList<>();
        featureList.forEach(feature->{
            if (!Objects.equals(feature.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
                productContextFeatureAggr.setModelCode(modelCode);
                productContextFeatureAggr.setFeatureCode(feature.getFeatureCode());
                productContextFeatureAggr.setFeatureGroup(featureGroupMap.get(feature.getGroup()));
                productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
                aggrs.add(productContextFeatureAggr);
                featureOptionMap.get(feature).forEach(option->{
                    ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
                    aggr.setModelCode(modelCode);
                    aggr.setFeatureCode(option.getFeatureCode());
                    aggr.setFeatureGroup(featureGroupMap.get(option.getGroup()));
                    aggr.setType(ProductContextFeatureEnum.OPTION.getType());
                    aggrs.add(aggr);
                });
            }
        });
        return aggrs;
    }

    /**
     * 更新product context时新建product context行
     * @param oldProductContextFeatureList
     * @param featureList
     * @param featureOptionMap
     * @param modelCode
     * @param featureGroupMap
     * @return
     */
    public static List<ProductContextFeatureAggr> createAddedProductContextFeatureList(List<ProductContextFeatureAggr> oldProductContextFeatureList,List<OxoRowsQry> featureList, Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap, String modelCode,Map<String,Long> featureGroupMap){
        List<ProductContextFeatureAggr> aggrs = new ArrayList<>();
        Set<String> existFeatureCodeSet = new HashSet<>();
        //先记录已有行
        oldProductContextFeatureList.forEach(aggr->{
            existFeatureCodeSet.add(aggr.getFeatureCode());
        });
        featureList.forEach(feature->{
            if (!Objects.equals(feature.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                //如果不存在，就新增
                if (!existFeatureCodeSet.contains(feature.getFeatureCode())){
                    ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
                    productContextFeatureAggr.setModelCode(modelCode);
                    productContextFeatureAggr.setFeatureCode(feature.getFeatureCode());
                    productContextFeatureAggr.setFeatureGroup(featureGroupMap.get(feature.getGroup()));
                    productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
                    aggrs.add(productContextFeatureAggr);
                }
                featureOptionMap.get(feature).forEach(option->{
                    if (!existFeatureCodeSet.contains(option.getFeatureCode())){
                        ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
                        aggr.setModelCode(modelCode);
                        aggr.setFeatureCode(option.getFeatureCode());
                        aggr.setFeatureGroup(featureGroupMap.get(option.getGroup()));
                        aggr.setType(ProductContextFeatureEnum.OPTION.getType());
                        aggrs.add(aggr);
                    }
                });
            }
        });
        return aggrs;
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
        Long modelYearFeatureGroupId = featureModelYearAggr.getParent().getId();
        //先添加AF00
        ProductContextFeatureAggr productContextFeatureAggr = new ProductContextFeatureAggr();
        productContextFeatureAggr.setModelCode(modelCode);
        productContextFeatureAggr.setFeatureCode(featureModelYearAggr.getFeatureCode());
        productContextFeatureAggr.setFeatureGroup(modelYearFeatureGroupId);
        productContextFeatureAggr.setType(ProductContextFeatureEnum.FEATURE.getType());
        productContextFeatureAggrList.add(productContextFeatureAggr);
        //再添加AF00下面的option
        optionAggrList.forEach(option->{
            ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
            aggr.setModelCode(modelCode);
            aggr.setFeatureCode(option.getFeatureCode());
            aggr.setFeatureGroup(modelYearFeatureGroupId);
            aggr.setType(ProductContextFeatureEnum.OPTION.getType());
            productContextFeatureAggrList.add(aggr);
            //记录modelYear的displayname与optionCode的对应关系，用于后续打勾
            modelYearMap.put(option.getDisplayName(),option.getFeatureCode());
        });
    }

    public static void createAddedModelYearProductContextFeature(List<ProductContextFeatureAggr> productContextFeatureAggrList, FeatureAggr featureModelYearAggr,String modelCode,Map<String,String> modelYearMap,List<ProductContextFeatureAggr> oldProductContextFeatureList,List<String> addedModelYearList){
        List<FeatureAggr> optionAggrList = featureModelYearAggr.getChildrenList();
        Long modelYearFeatureGroupId = featureModelYearAggr.getParent().getId();
        Set<String> existModelYearSet = new HashSet<>();
        //记录原有所有modelYear相关行的model Code
        oldProductContextFeatureList.forEach(row->{
            if (Objects.equals(row.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO), ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                existModelYearSet.add(row.getFeatureCode());
            }
        });
        optionAggrList.forEach(option->{
            if (!existModelYearSet.contains(option.getFeatureCode())){
                ProductContextFeatureAggr aggr = new ProductContextFeatureAggr();
                aggr.setModelCode(modelCode);
                aggr.setFeatureCode(option.getFeatureCode());
                aggr.setFeatureGroup(modelYearFeatureGroupId);
                aggr.setType(ProductContextFeatureEnum.OPTION.getType());
                productContextFeatureAggrList.add(aggr);
                addedModelYearList.add(option.getFeatureCode());
            }
            //记录modelYear的displayname与optionCode的对应关系，用于后续打勾
            modelYearMap.put(option.getDisplayName(),option.getFeatureCode());
        });
    }
}
