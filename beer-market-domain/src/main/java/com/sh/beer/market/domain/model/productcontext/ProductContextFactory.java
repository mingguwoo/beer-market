package com.sh.beer.market.domain.model.productcontext;

import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/8/10
 */
@Component
public class ProductContextFactory {

    /**
     * 初始化product context时构建product context打勾信息
     * @param productContextList
     * @param featureList
     * @param OxoListRespDto
     * @return
     */
    /*public static void createProductContextList(List<ProductContextAggr> productContextList,List<OxoRowsQry> featureList, OxoListRespDto OxoListRespDto,List<ProductContextAggr> addProductContextAggrList, List<ProductContextAggr> removeProductContextAggrList,Set<ProductContextAggr> existProductContextSet,String owner){
        Map<Long,OxoRowsQry> rowMap = new HashMap<>();
        Map<Long,OxoHeadQry> headMap = new HashMap<>();
        Map<String,String> optionFeatureMap = new HashMap<>();
        List<OxoEditCmd> pointRecord = new ArrayList<>();
        List<ProductContextAggr> newProductContextAggrList = new ArrayList<>();
        if (Objects.nonNull(productContextList)){
            productContextList.forEach(aggr->{
                //原有记录
                existProductContextSet.add(aggr);
            });
        }
        recordOxoRelationship(OxoListRespDto,featureList,rowMap,headMap,optionFeatureMap,pointRecord);
        pointRecord.forEach(point-> {
                    //将AF00以外的所有信息打勾
                if (!Objects.equals(rowMap.get(point.getRowId()).getFeatureCode().substring(CommonConstants.INT_ZERO, CommonConstants.INT_TWO), ConfigConstants.FEATURE_CODE_AF00.substring(CommonConstants.INT_ZERO, CommonConstants.INT_TWO))) {
                    ProductContextAggr productContextAggr = new ProductContextAggr();
                    productContextAggr.setModelCode(headMap.get(point.getHeadId()).getModelCode());
                    productContextAggr.setModelYear(headMap.get(point.getHeadId()).getModelYear());
                    productContextAggr.setOptionCode(rowMap.get(point.getRowId()).getFeatureCode());
                    productContextAggr.setFeatureCode(optionFeatureMap.get(rowMap.get(point.getRowId()).getFeatureCode()));
                    newProductContextAggrList.add(productContextAggr);
                }
        });
        //判断找到要新增的点和要删除的点
        newProductContextAggrList.forEach(aggr->{
            //原本没有，现在有了，要新增
            if (!existProductContextSet.contains(aggr)){
                aggr.setCreateUser(owner);
                aggr.setUpdateUser(owner);
                addProductContextAggrList.add(aggr);
            }
        });

        existProductContextSet.forEach(aggr->{
            //原本有，现在没了，且不是车型年相关，要删除
            if (!newProductContextAggrList.contains(aggr) && !Objects.equals(aggr.getFeatureCode(),ConfigConstants.FEATURE_CODE_AF00)){
                removeProductContextAggrList.add(aggr);
            }
        });

    }

    *//**
     * 初始化product context时构建model year相关打勾信息
     * @param modelCode
     * @param modelYearList
     * @param modelYearMap
     * @param existProductContextSet
     * @param owner
     *//*
    public static void createModelYearProductContext(String modelCode,List<String> modelYearList,Map<String,String> modelYearMap,List<ProductContextAggr> addProductContextAggrList, List<ProductContextAggr> removeProductContextAggrList,Set<ProductContextAggr> existProductContextSet,String owner){
        List<ProductContextAggr> newModelYearList = new ArrayList<>();
        modelYearList.forEach(modelYear->{
            ProductContextAggr productContextAggr = new ProductContextAggr();
            productContextAggr.setModelCode(modelCode);
            productContextAggr.setModelYear(modelYear);
            productContextAggr.setOptionCode(modelYearMap.get(modelYear));
            productContextAggr.setFeatureCode(ConfigConstants.FEATURE_CODE_AF00);
            newModelYearList.add(productContextAggr);
        });

        //判断找到要新增的点和要删除的点
        newModelYearList.forEach(aggr->{
            //原本没有，现在有，要新增
            if (!existProductContextSet.contains(aggr)){
                aggr.setCreateUser(owner);
                aggr.setUpdateUser(owner);
                addProductContextAggrList.add(aggr);
            }
        });

        existProductContextSet.forEach(aggr->{
            //原本有，现在没有，如果是车型年相关的，要删除
            if(!newModelYearList.contains(aggr) && Objects.equals(aggr.getFeatureCode(),ConfigConstants.FEATURE_CODE_AF00)){
                removeProductContextAggrList.add(aggr);
            }
        });
    }


    *//**
     * 记录oxo中各行各列的关系
     * @param OxoListRespDto
     * @param featureList
     * @param rowMap
     * @param headMap
     * @param optionFeatureMap
     * @param pointRecord
     *//*
    private static void recordOxoRelationship(OxoListRespDto OxoListRespDto, List<OxoRowsQry> featureList, Map<Long,OxoRowsQry> rowMap, Map<Long,OxoHeadQry> headMap, Map<String,String> optionFeatureMap, List<OxoEditCmd> pointRecord){
        //记录headId与head的对应关系
        OxoListRespDto.getOxoHeadResps().forEach(head->{
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
    }*/

}
