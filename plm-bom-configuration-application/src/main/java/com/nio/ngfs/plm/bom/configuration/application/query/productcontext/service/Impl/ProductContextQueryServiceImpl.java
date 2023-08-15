package com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.Impl;

import com.nio.ngfs.plm.bom.configuration.application.query.productcontext.service.ProductContextQueryService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.FeatureConverter;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.GetProductContextQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.GetProductContextRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextFeatureRowDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextOptionRowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
@Component
@RequiredArgsConstructor
public class ProductContextQueryServiceImpl implements ProductContextQueryService {

    private final FeatureConverter featureConverter;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public GetProductContextRespDto filter(List<ProductContextDto> pointList, List<BomsProductContextFeatureEntity> rowList, GetProductContextQry qry){
        GetProductContextRespDto getProductContextRespDto = new GetProductContextRespDto();
        Set<String> codeRecord = new HashSet<>();
        pointList.forEach(dto->{
            if (Objects.nonNull(dto.getFeatureCode())){
                codeRecord.add(dto.getFeatureCode());
            }
            if (Objects.nonNull(dto.getOptionCode())){
                codeRecord.add(dto.getOptionCode());
            }
        });
        List<FeatureAggr> featureAggrList = featureConverter.convertEntityListToDoList(bomsFeatureLibraryDao.queryByFeatureOptionCodeList(codeRecord.stream().toList()));
        Map<String,FeatureAggr> featureAggrMap = new HashMap<>();
        //将featureCode与FeatureAggr对应上
        featureAggrList.forEach(featureAggr -> featureAggrMap.put(featureAggr.getFeatureId().getFeatureCode(),featureAggr));
        //筛选catalog
        if (Objects.nonNull(qry.getCataLog())){
            pointList = pointList.stream().filter(point-> Objects.equals(featureAggrMap.get(point.getFeatureCode()).getCatalog(),qry.getCataLog())
                    || Objects.equals(featureAggrMap.get(point.getOptionCode()).getCatalog(),qry.getCataLog())).toList();
            rowList = rowList.stream().filter(row->Objects.equals(featureAggrMap.get(row.getFeatureCode()).getCatalog(),qry.getCataLog())).toList();
        }
        //将点和行对应上
        Map<String,BomsProductContextFeatureEntity> rowMap = new HashMap<>();
        Map<ProductContextDto,BomsProductContextFeatureEntity> pointMap = new HashMap<>();
        rowList.forEach(row->rowMap.put(row.getFeatureCode(),row));
        //需要考虑该行是option还是feature
        pointList.forEach(point->{
            //如果是feature
            if (Objects.equals(rowMap.get(point.getFeatureCode()).getType(), ProductContextFeatureEnum.feature.getType())){
                pointMap.put(point,rowMap.get(point.getFeatureCode()));
            }
            //如果是option
            if (Objects.equals(rowMap.get(point.getFeatureCode()).getType(),ProductContextFeatureEnum.option.getType())){
                pointMap.put(point,rowMap.get(point.getOptionCode()));
            }
        });
        //筛选featureGroup
        if (Objects.nonNull(qry.getFeatureGroup()) && !qry.getFeatureGroup().isEmpty()){

            //先筛选行
            rowList = rowList.stream().filter(row->qry.getFeatureGroup().contains(row.getFeatureGroup())).toList();
            //再筛选点
            List<BomsProductContextFeatureEntity> finalRowList = rowList;
            pointList = pointList.stream().filter(point-> finalRowList.contains(pointMap.get(point))).toList();
        }
        //模糊搜索，筛选optionCode featureCode
        pointList = pointList.stream().filter(point-> {
            return matchSearch(point.getOptionCode(), qry.getFeature()) ||
                    matchSearch(point.getFeatureCode(),qry.getFeature()) ||
                    matchSearch(featureAggrMap.get(point.getFeatureCode()).getDisplayName(),qry.getFeature());

        }).toList();
        rowList = pointList.stream().map(point->pointMap.get(point)).toList();
        //组装Dto
        Map<String,ProductContextFeatureRowDto> featureRowDtoMap = new HashMap<>();
        //组装行
            //先存Feature
        rowList.forEach(row-> {
            //如果是Feature
            if (Objects.equals(row.getType(), ProductContextFeatureEnum.feature.getType())){
                    ProductContextFeatureRowDto productContextFeatureRowDto = new ProductContextFeatureRowDto();
                    productContextFeatureRowDto.setRowId(row.getId());
                    productContextFeatureRowDto.setFeatureCode(row.getFeatureCode());
                    productContextFeatureRowDto.setDisplayName(featureAggrMap.get(row.getFeatureCode()).getDisplayName());
                    getProductContextRespDto.getProductContextFeatureRowDtoList().add(productContextFeatureRowDto);
                    featureRowDtoMap.put(row.getFeatureCode(),productContextFeatureRowDto);
            }
        });
            //再存Option
        rowList.forEach(row->{
            if (Objects.equals(row.getType(), ProductContextFeatureEnum.option.getType())) {
                ProductContextOptionRowDto productContextOptionRowDto = new ProductContextOptionRowDto();
                //获取父级feature的code
                String parentCode = featureAggrMap.get(row.getFeatureCode()).getParentFeatureCode();
                productContextOptionRowDto.setDisplayName(featureAggrMap.get(row.getFeatureCode()).getDisplayName());
                productContextOptionRowDto.setRowId(row.getId());
                productContextOptionRowDto.setFeatureCode(row.getFeatureCode());
                featureRowDtoMap.get(parentCode).getOptionRowList().add(productContextOptionRowDto);
            }
        });
        //组装列
        return null;
    }

    private boolean matchSearch(String content, String search){
        return content != null && content.contains(search);
    }
}
