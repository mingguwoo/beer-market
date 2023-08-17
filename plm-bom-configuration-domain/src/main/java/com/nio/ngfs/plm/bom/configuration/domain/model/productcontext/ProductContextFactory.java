package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Component
public class ProductContextFactory {

    /**
     * 初始化product context时构建product context打勾信息
     * @param featureList
     * @param oxoListQry
     * @return
     */
    public static List<ProductContextAggr> createProductContextList(List<OxoRowsQry> featureList, OxoListQry oxoListQry){
        Map<Long,OxoRowsQry> rowMap = new HashMap<>();
        Map<Long,OxoHeadQry> headMap = new HashMap<>();
        Map<String,String> optionFeatureMap = new HashMap<>();
        List<OxoEditCmd> pointRecord = new ArrayList<>();
        List<ProductContextAggr> productContextAggrs = new ArrayList<>();
        recordOxoRelationship(oxoListQry,featureList,rowMap,headMap,optionFeatureMap,pointRecord);
        pointRecord.forEach(point->{
            //将AF00以外的所有信息打勾
            if (!Objects.equals(rowMap.get(point.getHeadId()).getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                ProductContextAggr productContextAggr = new ProductContextAggr();
                productContextAggr.setModelCode(headMap.get(point.getHeadId()).getModelCode());
                productContextAggr.setModelYear(headMap.get(point.getHeadId()).getModelYear());
                productContextAggr.setOptionCode(rowMap.get(point.getRowId()).getFeatureCode());
                productContextAggr.setFeatureCode(optionFeatureMap.get(rowMap.get(point.getRowId()).getFeatureCode()));
                productContextAggrs.add(productContextAggr);
            }
        });
        return productContextAggrs;
    }

    /**
     * 更新product context时构建新增的product context打勾信息
     * @param oldProductContextAggrList
     * @param featureList
     * @param oxoListQry
     * @return
     */
    public static List<ProductContextAggr> createAddedProductContextList(List<ProductContextAggr> oldProductContextAggrList,List<OxoRowsQry> featureList, OxoListQry oxoListQry){
        Map<Long,OxoRowsQry> rowMap = new HashMap<>();
        Map<Long,OxoHeadQry> headMap = new HashMap<>();
        Map<String,String> optionFeatureMap = new HashMap<>();
        List<OxoEditCmd> pointRecord = new ArrayList<>();
        List<ProductContextAggr> productContextAggrs = new ArrayList<>();
        recordOxoRelationship(oxoListQry,featureList,rowMap,headMap,optionFeatureMap,pointRecord);
        Set<String> existProductContextSet = new HashSet<>();
        oldProductContextAggrList.forEach(aggr->{
            existProductContextSet.add(aggr.getOptionCode());
        });
        pointRecord.forEach(point->{
            if (!existProductContextSet.contains(rowMap.get(point.getRowId()).getFeatureCode())){
                ProductContextAggr productContextAggr = new ProductContextAggr();
                productContextAggr.setModelCode(headMap.get(point.getHeadId()).getModelCode());
                productContextAggr.setModelYear(headMap.get(point.getHeadId()).getModelYear());
                productContextAggr.setOptionCode(rowMap.get(point.getRowId()).getFeatureCode());
                productContextAggr.setFeatureCode(optionFeatureMap.get(rowMap.get(point.getRowId()).getFeatureCode()));
                productContextAggrs.add(productContextAggr);
            }
        });
        return productContextAggrs;
    }

    /**
     * 初始化product context时构建model year相关打勾信息
     * @param productContextAggrList
     * @param modelCode
     * @param modelYearList
     * @param modelYearMap
     */
    public static void createModelYearProductContext(List<ProductContextAggr> productContextAggrList, String modelCode,List<String> modelYearList,Map<String,String> modelYearMap){
        modelYearList.forEach(modelYear->{
            ProductContextAggr productContextAggr = new ProductContextAggr();
            productContextAggr.setModelCode(modelCode);
            productContextAggr.setModelYear(modelYear);
            productContextAggr.setOptionCode(modelYearMap.get(modelYear));
            productContextAggr.setFeatureCode(ConfigConstants.FEATURE_CODE_AF00);
            productContextAggrList.add(productContextAggr);
        });
    }

    /**
     * 更新product context时新增model year相关打勾信息
     * @param productContextAggrList
     * @param modelCode
     * @param modelYearList
     * @param modelYearMap
     */
    public static void createAddedModelYearProductContextFeature(List<ProductContextAggr> oldProductContextList,List<ProductContextAggr> productContextAggrList, String modelCode,List<String> modelYearList,Map<String,String> modelYearMap){
        Set<String> oldModelYearSet = new HashSet<>();
        //获取所有原先model year的option code
        oldProductContextList.forEach(oldProductContext->{
            oldModelYearSet.add(oldProductContext.getOptionCode());
        });
        modelYearList.forEach(modelYear->{
            if (oldModelYearSet.contains(modelYearMap.get(modelYear))){
                ProductContextAggr productContextAggr = new ProductContextAggr();
                productContextAggr.setModelCode(modelCode);
                productContextAggr.setModelYear(modelYear);
                productContextAggr.setOptionCode(modelYearMap.get(modelYear));
                productContextAggr.setFeatureCode(ConfigConstants.FEATURE_CODE_AF00);
                productContextAggrList.add(productContextAggr);
            }
        });
    }

    /**
     * 记录oxo中各行各列的关系
     * @param oxoListQry
     * @param featureList
     * @param rowMap
     * @param headMap
     * @param optionFeatureMap
     * @param pointRecord
     */
    private static void recordOxoRelationship(OxoListQry oxoListQry,List<OxoRowsQry> featureList,Map<Long,OxoRowsQry> rowMap,Map<Long,OxoHeadQry> headMap,Map<String,String> optionFeatureMap,List<OxoEditCmd> pointRecord){
        //记录headId与head的对应关系
        oxoListQry.getOxoHeadResps().forEach(head->{
            head.getRegionInfos().forEach(regionInfo -> {
                regionInfo.getDriveHands().forEach(driveHandInfo -> {
                    driveHandInfo.getSalesVersionInfos().forEach(salesVersionInfo -> {
                        headMap.put(salesVersionInfo.getHeadId(),head);
                    });
                });
            });
        });
        featureList.forEach(feature->{
            //记录rowId的对应关系
            feature.getOptions().forEach(option->{
                rowMap.put(option.getRowId(),option);
                //记录option的父级feature的code
                optionFeatureMap.put(option.getFeatureCode(),feature.getFeatureCode());
            });
            //记录要生成的点
            feature.getPackInfos().forEach(packageInfo->{
                //空心或实心圈则需要生成product context
                if (Objects.equals(packageInfo.getPackageCode(), OxoOptionPackageTypeEnum.DEFALUT.getType())|| Objects.equals(packageInfo.getPackageCode(),OxoOptionPackageTypeEnum.AVAILABLE.getType())){
                    pointRecord.add(packageInfo);
                }
            });
        });
    }

}
