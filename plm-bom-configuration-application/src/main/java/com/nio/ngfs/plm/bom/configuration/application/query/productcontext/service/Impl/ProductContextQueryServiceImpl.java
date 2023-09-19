package com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.Impl;

import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.ProductContextQueryService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.comparator.ProductContextFeatureRowComparator;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductContextQueryServiceImpl implements ProductContextQueryService {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    private final BomsOxoVersionSnapshotDao bomsOxoVersionSnapshotDao;

    @Override
    public GetProductContextRespDto filterAndBuildResponse(List<ProductContextDto> pointList, List<BomsProductContextFeatureEntity> rowList, GetProductContextQry qry){
        Map<String,BomsProductContextFeatureEntity> rowMap = new HashMap<>();
        Map<ProductContextDto,BomsProductContextFeatureEntity> pointMap = new HashMap<>();
        Map<String,String> groupRecordMap = new HashMap<>();
        Map<String,BomsFeatureLibraryEntity> featureAggrMap = buildOptionCodeFeatureAggrMap(pointList,rowList,groupRecordMap);
        //将featureCode和行对应上
        rowList.forEach(row->rowMap.put(row.getFeatureCode(),row));
        //将点与行对应上
        pointList.forEach(point->{
            pointMap.put(point,rowMap.get(point.getOptionCode()));
        });
        //筛选catalog
        if (Objects.nonNull(qry.getCatalog())){
            pointList = pointList.stream().filter(point-> Objects.equals(featureAggrMap.get(point.getFeatureCode()).getCatalog(),qry.getCatalog())
                    || Objects.equals(featureAggrMap.get(point.getOptionCode()).getCatalog(),qry.getCatalog()) || Objects.equals(point.getFeatureCode(), ConfigConstants.FEATURE_CODE_AF00)).toList();
            rowList = rowList.stream().filter(row->Objects.equals(featureAggrMap.get(row.getFeatureCode()).getCatalog(),qry.getCatalog())).toList();
        }
        //筛选featureGroup
        if (Objects.nonNull(qry.getGroupCode()) && !qry.getGroupCode().isEmpty()){
            //先筛选行
            rowList = rowList.stream().filter(row->qry.getGroupCode().contains(groupRecordMap.get(row.getFeatureCode()))).toList();
            //再筛选点
            List<BomsProductContextFeatureEntity> finalRowList = rowList;
            pointList = pointList.stream().filter(point-> finalRowList.contains(pointMap.get(point)) || Objects.equals(point.getFeatureCode(), ConfigConstants.FEATURE_CODE_AF00)).toList();
        }
        //模糊搜索，筛选optionCode, featureCode, optionDisplayName, featureDisplayName
        if (Objects.nonNull(qry.getFeature())){
            pointList = pointList.stream().filter(point-> matchSearch(point.getOptionCode(), qry.getFeature()) ||
                    matchSearch(point.getFeatureCode(),qry.getFeature()) ||
                    matchSearch(featureAggrMap.get(point.getFeatureCode()).getDisplayName(),qry.getFeature()) ||
                    matchSearch(featureAggrMap.get(point.getOptionCode()).getDisplayName(),qry.getFeature()) ||
                    Objects.equals(point.getFeatureCode(), ConfigConstants.FEATURE_CODE_AF00)).toList();
            Set<String> matchFeatureRowSet = new HashSet<>();
            Set<String> matchOptionRowSet = new HashSet<>();
            Set<String> matchOptionFatherSet = new HashSet<>();
            //存下所有满足模糊查询的option，row
            rowList.forEach(row-> {
                if (matchSearch(row.getFeatureCode(),qry.getFeature()) || matchSearch(featureAggrMap.get(row.getFeatureCode()).getDisplayName(),qry.getFeature())){
                    if (Objects.equals(row.getType(),ProductContextFeatureEnum.FEATURE.getType())){
                        matchFeatureRowSet.add(row.getFeatureCode());
                    }
                    else{
                        //如果option符合，则他的父级feature也符合
                        matchOptionFatherSet.add(featureAggrMap.get(row.getFeatureCode()).getParentFeatureCode());
                        matchOptionRowSet.add(row.getFeatureCode());
                    }
                }
            });
            rowList = rowList.stream().filter(row->{
                if (Objects.equals(row.getType(),ProductContextFeatureEnum.FEATURE.getType())){
                    return (matchFeatureRowSet.contains(row.getFeatureCode()) || matchOptionFatherSet.contains(row.getFeatureCode()));
                }
                else{
                    return (matchOptionRowSet.contains(row.getFeatureCode()));

                }
            }).toList();
        }
        //组装Dto
        return buildResponseDto(pointList,rowList,featureAggrMap,groupRecordMap);
    }

    private boolean matchSearch(String content, String search){
        return content != null && content.contains(search);
    }

    /**
     * 组装Response
     * @param pointList
     * @param rowList
     * @param featureAggrMap
     * @return
     */
    private GetProductContextRespDto buildResponseDto(List<ProductContextDto> pointList, List<BomsProductContextFeatureEntity> rowList,Map<String,BomsFeatureLibraryEntity> featureAggrMap,Map<String,String> groupRecordMap){
        Map<String,ProductContextFeatureRowDto> featureRowDtoMap = new HashMap<>();
        GetProductContextRespDto getProductContextRespDto = new GetProductContextRespDto();
        Map<String,Long> featureCodeRowIdMap = new HashMap<>();
        //组装行
            //先存Feature
        rowList.forEach(row-> {
            if (Objects.equals(row.getType(), ProductContextFeatureEnum.FEATURE.getType())){
                ProductContextFeatureRowDto productContextFeatureRowDto = new ProductContextFeatureRowDto();
                productContextFeatureRowDto.setRowId(row.getId());
                productContextFeatureRowDto.setCatalog(featureAggrMap.get(row.getFeatureCode()).getCatalog());
                productContextFeatureRowDto.setFeatureCode(row.getFeatureCode());
                productContextFeatureRowDto.setDisplayName(featureAggrMap.get(row.getFeatureCode()).getDisplayName());
                getProductContextRespDto.getProductContextFeatureRowDtoList().add(productContextFeatureRowDto);
                //记下featureCode对应的行信息
                featureRowDtoMap.put(row.getFeatureCode(),productContextFeatureRowDto);
            }
        });
        //再存Option
        rowList.forEach(row->{
            if (Objects.equals(row.getType(), ProductContextFeatureEnum.OPTION.getType())) {
                ProductContextOptionRowDto productContextOptionRowDto = new ProductContextOptionRowDto();
                //获取父级feature的code
                String parentCode = featureAggrMap.get(row.getFeatureCode()).getParentFeatureCode();
                productContextOptionRowDto.setDisplayName(featureAggrMap.get(row.getFeatureCode()).getDisplayName());
                productContextOptionRowDto.setRowId(row.getId());
                productContextOptionRowDto.setCatalog(featureAggrMap.get(row.getFeatureCode()).getCatalog());
                productContextOptionRowDto.setFeatureCode(row.getFeatureCode());
                featureCodeRowIdMap.put(row.getFeatureCode(),row.getId());
                //记下optionCode对应的行信息
                featureRowDtoMap.get(parentCode).getOptionRowList().add(productContextOptionRowDto);
            }
        });
        //组装列和列行记录表
        //  先处理列id
        List<String> modelModelYearList = new ArrayList<>();
        Map<ProductContextDto,Long> pointColumnIdMap = new HashMap<>();
        Set<Long> modelYearColumnIdSet = new HashSet<>();
        pointList.forEach(point->{
            if (!modelModelYearList.contains(point.getModelCode()+point.getModelYear())){
                modelModelYearList.add(point.getModelCode()+point.getModelYear());
            }
            //记录下该点对应的列id
            pointColumnIdMap.put(point, (long) modelModelYearList.indexOf(point.getModelCode()+point.getModelYear()));
            //记录下车型年相关的点的列id
            modelYearColumnIdSet.add((long) modelModelYearList.indexOf(point.getModelCode()+point.getModelYear()));
        });
        Set<Long> columnIdSet = new HashSet<>();
        pointList.forEach(point->{
            ProductContextColumnDto productContextColumnDto = new ProductContextColumnDto();
            ProductContextPointDto productContextPointDto = new ProductContextPointDto();
            productContextColumnDto.setColumnId(pointColumnIdMap.get(point));
            productContextColumnDto.setModelCode(point.getModelCode());
            productContextColumnDto.setModelYear(point.getModelYear());
            productContextPointDto.setColumnId(pointColumnIdMap.get(point));
            productContextPointDto.setRowId(featureCodeRowIdMap.get(point.getOptionCode()));
            getProductContextRespDto.getProductContextColumnDtoList().add(productContextColumnDto);
            getProductContextRespDto.getProductContextPointDtoList().add(productContextPointDto);
        });
        getProductContextRespDto.setProductContextColumnDtoList(getProductContextRespDto.getProductContextColumnDtoList().stream().distinct().toList());
        //筛选掉没被选中的列
        getProductContextRespDto.getProductContextPointDtoList().forEach(point->{
            columnIdSet.add(point.getColumnId());
        });
        getProductContextRespDto.setProductContextColumnDtoList(getProductContextRespDto.getProductContextColumnDtoList().stream().filter(column-> modelYearColumnIdSet.contains(column.getColumnId()) || columnIdSet.contains(column.getColumnId())).toList());
        sortProductContextRow(getProductContextRespDto,groupRecordMap);
        return getProductContextRespDto;
    };

    /**
     * 对feature和option行进行排序
     * @param productContext
     * @param groupRecordMap
     * @return
     */
    private void sortProductContextRow(GetProductContextRespDto productContext,Map<String,String> groupRecordMap){
        ProductContextFeatureRowComparator comparator = new ProductContextFeatureRowComparator(groupRecordMap);
        //先排option
        productContext.setProductContextFeatureRowDtoList(productContext.getProductContextFeatureRowDtoList().stream().map(feature->{
            feature.setOptionRowList(feature.getOptionRowList().stream().sorted(Comparator.comparing(ProductContextOptionRowDto::getFeatureCode)).toList());
            return feature;
        }).toList());
        //排feature
        productContext.setProductContextFeatureRowDtoList(productContext.getProductContextFeatureRowDtoList().stream().sorted(comparator).toList());
    }
    /**
     * 获取所有相关的featureEntity并将optionCode与featureEntity,groupEntity对应上
     * @param pointList
     * @return
     */
    private Map<String,BomsFeatureLibraryEntity> buildOptionCodeFeatureAggrMap(List<ProductContextDto> pointList,List<BomsProductContextFeatureEntity> rowList,Map<String,String> groupRecordMap){
        Set<String> codeRecord = new HashSet<>();
        Map<String,BomsFeatureLibraryEntity> featureAggrMap = new HashMap<>();
        pointList.forEach(dto->{
            if (Objects.nonNull(dto.getFeatureCode())){
                codeRecord.add(dto.getFeatureCode());
            }
            if (Objects.nonNull(dto.getOptionCode())){
                codeRecord.add(dto.getOptionCode());
            }
        });
        //需要考虑可能oxo行中有，但没被选中为点的情况
        rowList.forEach(entity->{
            codeRecord.add(entity.getFeatureCode());
        });
        List<BomsFeatureLibraryEntity> featureList = bomsFeatureLibraryDao.queryAll();
        featureList.forEach(feature->{
            if (codeRecord.contains(feature.getFeatureCode())){
                featureAggrMap.put(feature.getFeatureCode(),feature);
                //先存一遍feature与group的对应关系
                if (Objects.equals(feature.getType(),FeatureTypeEnum.FEATURE.getType())){
                    groupRecordMap.put(feature.getFeatureCode(),feature.getParentFeatureCode());
                }
            }
        });
        //再遍历一遍，存下option对应的group
        featureList.forEach(feature->{
            if (codeRecord.contains(feature.getFeatureCode()) && Objects.equals(feature.getType(),FeatureTypeEnum.OPTION.getType())){
                groupRecordMap.put(feature.getFeatureCode(),groupRecordMap.get(feature.getParentFeatureCode()));
            }
        });

        return featureAggrMap;
    }

    @Override
    public ProductContextOptionsRespDto queryProductContextOptions() {
        ProductContextOptionsRespDto productContextOptionsRespDto = new ProductContextOptionsRespDto();
        productContextOptionsRespDto.setModelCode(bomsOxoVersionSnapshotDao.queryAll().stream().map(snapShot->snapShot.getModelCode()).distinct().toList());
        productContextOptionsRespDto.setGroupCode(bomsFeatureLibraryDao.getGroupList().stream().map(group->group.getFeatureCode()).toList());
        return productContextOptionsRespDto;
    }
}
