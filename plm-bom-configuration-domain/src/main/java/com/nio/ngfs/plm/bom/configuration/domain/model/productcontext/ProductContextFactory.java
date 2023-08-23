package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
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
     * @param productContextList
     * @param featureList
     * @param oxoListQry
     * @return
     */
    public static void createProductContextList(List<ProductContextAggr> productContextList,List<OxoRowsQry> featureList, OxoListQry oxoListQry){
        Map<Long,OxoRowsQry> rowMap = new HashMap<>();
        Map<Long,OxoHeadQry> headMap = new HashMap<>();
        Map<String,String> optionFeatureMap = new HashMap<>();
        List<OxoEditCmd> pointRecord = new ArrayList<>();
        Set<ProductContextAggr> existProductContextSet = new HashSet<>();
        if (Objects.nonNull(productContextList)){
            productContextList.forEach(aggr->{
                existProductContextSet.add(aggr);
            });
        }
        recordOxoRelationship(oxoListQry,featureList,rowMap,headMap,optionFeatureMap,pointRecord);
        pointRecord.forEach(point->{
            //将AF00以外的所有信息打勾
            if (!Objects.equals(rowMap.get(point.getRowId()).getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO),ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                ProductContextAggr productContextAggr = new ProductContextAggr();
                productContextAggr.setModelCode(headMap.get(point.getHeadId()).getModelCode());
                productContextAggr.setModelYear(headMap.get(point.getHeadId()).getModelYear());
                productContextAggr.setOptionCode(rowMap.get(point.getRowId()).getFeatureCode());
                productContextAggr.setFeatureCode(optionFeatureMap.get(rowMap.get(point.getRowId()).getFeatureCode()));
                if (!existProductContextSet.contains(productContextAggr)){
                    productContextList.add(productContextAggr);
                }
            }
        });
    }

    /**
     * 初始化product context时构建model year相关打勾信息
     * @param productContextList
     * @param modelCode
     * @param modelYearList
     * @param modelYearMap
     */
    public static void createModelYearProductContext(List<ProductContextAggr> productContextList, String modelCode,List<String> modelYearList,Map<String,String> modelYearMap){
        Set<ProductContextAggr> oldModelYearSet = new HashSet<>();
        //获取所有原先model year的option code
        if (Objects.nonNull(productContextList)) {
            productContextList.forEach(oldProductContext->{
                oldModelYearSet.add(oldProductContext);
            });
        }
        modelYearList.forEach(modelYear->{
            ProductContextAggr productContextAggr = new ProductContextAggr();
            productContextAggr.setModelCode(modelCode);
            productContextAggr.setModelYear(modelYear);
            productContextAggr.setOptionCode(modelYearMap.get(modelYear));
            productContextAggr.setFeatureCode(ConfigConstants.FEATURE_CODE_AF00);
            if (!oldModelYearSet.contains(productContextAggr)){
                productContextList.add(productContextAggr);
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
                //记录要生成的点
                if (Objects.nonNull(option.getPackInfos())){
                    option.getPackInfos().forEach(packageInfo->{
                        //空心或实心圈则需要生成product context
                        if (Objects.equals(packageInfo.getPackageCode(), OxoOptionPackageTypeEnum.DEFALUT.getType())|| Objects.equals(packageInfo.getPackageCode(),OxoOptionPackageTypeEnum.AVAILABLE.getType())){
                            pointRecord.add(packageInfo);
                        }
                    });
                }
            });
        });
    }

}
