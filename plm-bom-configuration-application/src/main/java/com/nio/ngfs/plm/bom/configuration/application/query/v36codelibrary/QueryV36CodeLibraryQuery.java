package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums.V36CodeLibraryTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.QueryV36CodeLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.QueryV36CodeLibraryRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.SalesFeatureDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.V36CodeLibraryDigitDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.V36CodeLibraryOptionDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class QueryV36CodeLibraryQuery {

    private final BomsV36CodeLibraryDao bomsV36CodeLibraryDao;
    private final V36CodeLibraryApplicationService v36CodeLibraryApplicationService;

    public QueryV36CodeLibraryRespDto execute(QueryV36CodeLibraryQry qry){
        //先查找所有
        List<BomsV36CodeLibraryEntity> v36CodeLibraryEntityList = bomsV36CodeLibraryDao.queryAll();
        QueryV36CodeLibraryRespDto queryV36CodeLibraryRespDto = new QueryV36CodeLibraryRespDto(new ArrayList<>());
        Map<String, FeatureAggr> featureMap = v36CodeLibraryApplicationService.queryAllSalesFeature();
        //记录所有对应关系
        Map<Long,BomsV36CodeLibraryEntity> entityMap = new HashMap<>();
        //记录digit
        Map<Long, V36CodeLibraryDigitDto> digitMap = new HashMap<>();
        //记录digit下面的option
        Map<Long,List<V36CodeLibraryOptionDto>> digitOptionMap = new HashMap<>();
        //先记录直接被搜到的digit
        Set<Long> digitSet = new HashSet<>();
        //先记录id的entity并筛选
        List<BomsV36CodeLibraryEntity> matchEntityList = v36CodeLibraryEntityList.stream().map(entity->{
            entityMap.put(entity.getId(),entity);
            return entity;
        }).filter(entity-> matchSearch(entity.getSalesFeatureList(),qry.getSalesFeature()) && ( matchDigitSalesFeature(entity,featureMap,qry.getName()) || matchSearch(entity.getCode(),qry.getName()) || matchSearch(entity.getChineseName(),qry.getName()) || matchSearch(entity.getDisplayName(),qry.getName()) ))
                .map(entity->{
                    //先记录下被搜到的digit，到时候直接添加他所有的option
                    if (Objects.equals(entity.getType(),V36CodeLibraryTypeEnum.DIGIT.getType())){
                        digitSet.add(entity.getId());
                    }
                    return entity;
                })
                .toList();
        if (matchEntityList.isEmpty()){
            return queryV36CodeLibraryRespDto;
        }
        //记录下digit与option的对应关系
        v36CodeLibraryEntityList.forEach(entity->{
            if (Objects.equals(entity.getType(),V36CodeLibraryTypeEnum.OPTION.getType())){
                V36CodeLibraryOptionDto dto = buildOption(entity);
                //如果前面存了，直接添加到list
                if (digitOptionMap.containsKey(entity.getParentId())){
                    digitOptionMap.get(entity.getParentId()).add(dto);
                }
                else{
                    //如果前面没存，建个list放进去
                    List<V36CodeLibraryOptionDto> optionList = new ArrayList<>();
                    optionList.add(dto);
                    digitOptionMap.put(entity.getParentId(),optionList);
                }
            }
        });
        //先存下所有被选中的digit
        digitSet.forEach(id->{
            V36CodeLibraryDigitDto dto = buildDigit(entityMap.get(id),featureMap);
            dto.setOptionList(digitOptionMap.get(id));
            queryV36CodeLibraryRespDto.getDigitList().add(dto);
        });

        //再存下没有被涵盖进去的option
        matchEntityList.forEach(entity->{
            //如果不是被搜索中的digit，且父级digit也没有被直接搜索到
            if (!digitSet.contains(entity.getId()) && !digitSet.contains(entity.getParentId())){
                //如果没存过父级，就要新建
                if (!digitMap.containsKey(entity.getParentId())){
                    V36CodeLibraryDigitDto digitDto = buildDigit(entityMap.get(entity.getParentId()),featureMap);
                    V36CodeLibraryOptionDto optionDto = buildOption(entity);
                    List<V36CodeLibraryOptionDto> optionList = new ArrayList<>();
                    optionList.add(optionDto);
                    digitDto.setOptionList(optionList);
                    queryV36CodeLibraryRespDto.getDigitList().add(digitDto);
                    digitMap.put(entity.getParentId(),digitDto);
                }
                //如果存过父级，直接加进去
                else{
                    digitMap.get(entity.getParentId()).getOptionList().add(buildOption(entity));
                }
            }
        });
        //排序
        queryV36CodeLibraryRespDto.setDigitList(queryV36CodeLibraryRespDto.getDigitList().stream().sorted(Comparator.comparingInt((V36CodeLibraryDigitDto digit)->Integer.parseInt(StringUtils.substringBefore(digit.getCode(),"-"))).thenComparing((V36CodeLibraryDigitDto o1, V36CodeLibraryDigitDto o2)->{
            return o1.getCreateTimeInDate().compareTo(o2.getCreateTimeInDate());
        })).toList());
        queryV36CodeLibraryRespDto.setDigitList(queryV36CodeLibraryRespDto.getDigitList().stream().map(digit->{
            if (Objects.nonNull(digit.getOptionList())){
                digit.setOptionList(digit.getOptionList().stream().sorted(Comparator.comparing((V36CodeLibraryOptionDto dto)->dto.getCode().charAt(0)).thenComparing((V36CodeLibraryOptionDto o1, V36CodeLibraryOptionDto o2)->{
                    return o1.getCreateTimeInDate().compareTo(o2.getCreateTimeInDate());
                })).toList());
            }
            return digit;
        }).toList());
        return queryV36CodeLibraryRespDto;
    }
    private boolean matchSearch(String content, String search){
        if (Objects.nonNull(search)){
            return content != null && content.contains(search);
        }
        return true;
    }

    private V36CodeLibraryDigitDto buildDigit(BomsV36CodeLibraryEntity entity,Map<String,FeatureAggr> featureMap){
        V36CodeLibraryDigitDto dto = new V36CodeLibraryDigitDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setChineseName(entity.getChineseName());
        dto.setRemark(entity.getRemark());
        dto.setDisplayName(entity.getDisplayName());
        dto.setCreateUser(entity.getCreateUser());
        dto.setCreateTime(String.valueOf(entity.getCreateTime()));
        dto.setUpdateUser(entity.getUpdateUser());
        dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
        dto.setCreateTimeInDate(entity.getCreateTime());
        List<String> v36SalesFeatureCodeList = Arrays.asList(entity.getSalesFeatureList().split(","));
        //加个兜底，理论上不会出现salesFeatureList为空的情况
        if (!StringUtils.isBlank(entity.getSalesFeatureList())){
            StringBuilder featureCode = new StringBuilder("");
            StringBuilder featureDisplayName = new StringBuilder("");
            StringBuilder featureChineseName = new StringBuilder("");
            v36SalesFeatureCodeList.forEach(salesFeatureCode->{
                if (!featureCode.isEmpty()){
                    featureCode.append("|");
                }
                if (!featureDisplayName.isEmpty()){
                    featureDisplayName.append("|");
                }
                if (!featureChineseName.isEmpty()){
                    featureChineseName.append("|");
                }
                featureCode.append(salesFeatureCode);
                featureDisplayName.append(featureMap.get(salesFeatureCode).getDisplayName());
                featureChineseName.append(featureMap.get(salesFeatureCode).getChineseName());
            });
            SalesFeatureDto salesFeatureDto = new SalesFeatureDto();
            salesFeatureDto.setFeatureCode(String.valueOf(featureCode));
            salesFeatureDto.setDisplayName(String.valueOf(featureDisplayName));
            salesFeatureDto.setChineseName(String.valueOf(featureChineseName));
            dto.setSalesFeatureList(salesFeatureDto);
        }
        return dto;
    }

    private V36CodeLibraryOptionDto buildOption(BomsV36CodeLibraryEntity entity){
        V36CodeLibraryOptionDto dto = new V36CodeLibraryOptionDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setChineseName(entity.getChineseName().replace(",","|"));
        dto.setRemark(entity.getRemark());
        dto.setDisplayName(entity.getDisplayName().replace(",","|"));
        dto.setSalesFeatureList(entity.getSalesFeatureList().replace(",","|"));
        dto.setCreateUser(entity.getCreateUser());
        dto.setCreateTime(String.valueOf(entity.getCreateTime()));
        dto.setUpdateUser(entity.getUpdateUser());
        dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
        dto.setCreateTimeInDate(entity.getCreateTime());
        return dto;
    }

    private boolean matchDigitSalesFeature(BomsV36CodeLibraryEntity entity, Map<String, FeatureAggr> featureMap, String name){
        AtomicBoolean isMatch = new AtomicBoolean(false);
        if (!Objects.equals(entity.getType(),V36CodeLibraryTypeEnum.DIGIT.getType()) || (StringUtils.isBlank(entity.getSalesFeatureList()))){
            return isMatch.get();
        }
        List<String> featureList = Arrays.asList(entity.getSalesFeatureList().split(","));
        featureList.forEach(featureCode->{
            if (isMatch.get()){
                return;
            }
            if (matchSearch(featureMap.get(featureCode).getDisplayName(),name) || matchSearch(featureMap.get(featureCode).getChineseName(),name)){
                isMatch.set(true);
            }
        });
        return isMatch.get();
    }
}
