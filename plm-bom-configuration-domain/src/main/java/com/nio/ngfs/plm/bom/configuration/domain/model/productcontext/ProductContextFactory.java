package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
public class ProductContextFactory {

    public static List<ProductContextAggr> createProductContextListFromOxo(List<OxoRowsQry> featureList, Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap, OxoListQry oxoListQry){
        Map<Long,OxoRowsQry> rowMap = new HashMap<>();
        Map<Long,OxoHeadQry> headMap = new HashMap<>();
        Map<String,String> optionFeatureMap = new HashMap<>();
        List<OxoEditCmd> pointRecord = new ArrayList<>();
        List<ProductContextAggr> productContextAggrs = new ArrayList<>();
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
        pointRecord.forEach(point->{
            ProductContextAggr productContextAggr = new ProductContextAggr();
            productContextAggr.setModelCode(headMap.get(point.getHeadId()).getModelCode());
            productContextAggr.setModelYear(headMap.get(point.getHeadId()).getModelYear());
            productContextAggr.setOptionCode(rowMap.get(point.getRowId()).getFeatureCode());
            productContextAggr.setFeatureCode(optionFeatureMap.get(rowMap.get(point.getRowId()).getFeatureCode()));
            productContextAggrs.add(productContextAggr);
        });
        return productContextAggrs;
    }
}
