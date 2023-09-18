package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.enums.V36CodeLibraryTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.QueryV36CodeLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.QueryV36CodeLibraryRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.V36CodeLibraryDigitDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.V36CodeLibraryOptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class QueryV36CodeLibraryQuery {

    private final BomsV36CodeLibraryDao bomsV36CodeLibraryDao;

    public QueryV36CodeLibraryRespDto execute(QueryV36CodeLibraryQry qry){
        //先查找所有
        List<BomsV36CodeLibraryEntity> v36CodeLibraryEntityList = bomsV36CodeLibraryDao.queryAll();
        QueryV36CodeLibraryRespDto queryV36CodeLibraryRespDto = new QueryV36CodeLibraryRespDto();
        List<V36CodeLibraryDigitDto> digitList = new ArrayList<>();
        queryV36CodeLibraryRespDto.setDigitList(digitList);
        Map<Long,BomsV36CodeLibraryEntity> entityMap = new HashMap<>();
        Map<Long, V36CodeLibraryDigitDto> digitMap = new HashMap<>();
        //先记录id的entity并筛选
        List<BomsV36CodeLibraryEntity> matchEntityList = v36CodeLibraryEntityList.stream().map(entity->{
            entityMap.put(entity.getId(),entity);
            return entity;
        }).filter(entity-> matchSearch(entity.getSalesFeatureList(),qry.getSalesFeature()) && ( matchSearch(entity.getChineseName(),qry.getName()) || matchSearch(entity.getDisplayName(),qry.getName()) )).toList();
        if (matchEntityList.isEmpty()){
            return queryV36CodeLibraryRespDto;
        }
        matchEntityList.forEach(entity->{
            //如果是digit
            if (Objects.equals(entity.getType(),V36CodeLibraryTypeEnum.DIGIT.getType())){
                //如果没有就新建，如果有就跳过
                if (!digitMap.containsKey(entity.getId())){
                    buildDigit(entity,queryV36CodeLibraryRespDto,digitMap);
                }
            }
            if (Objects.equals(entity.getType(),V36CodeLibraryTypeEnum.OPTION.getType())){
                //如果没有父级,就创建一个
                if (!digitMap.containsKey(entity.getParentId())){
                    buildDigit(entityMap.get(entity.getParentId()),queryV36CodeLibraryRespDto,digitMap);
                }
                //在父级digit的option列表下添加option
                V36CodeLibraryOptionDto dto = new V36CodeLibraryOptionDto();
                dto.setCode(entity.getCode());
                dto.setChineseName(entity.getChineseName().replace(",","|"));
                dto.setRemark(entity.getRemark());
                dto.setDisplayName(entity.getDisplayName().replace(",","|"));
                dto.setSalesFeatureList(entity.getSalesFeatureList().replace(",","|"));
                dto.setCreateUser(entity.getCreateUser());
                dto.setCreateTime(String.valueOf(entity.getCreateTime()));
                dto.setUpdateUser(entity.getUpdateUser());
                dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
                digitMap.get(entity.getParentId()).getOptionList().add(dto);
            }
        });
        queryV36CodeLibraryRespDto.setDigitList(queryV36CodeLibraryRespDto.getDigitList().stream().sorted(Comparator.comparing(V36CodeLibraryDigitDto::getCode).thenComparing(V36CodeLibraryDigitDto::getCreateTime)).toList());
        queryV36CodeLibraryRespDto.getDigitList().stream().map(digit->{
            digit.setOptionList(digit.getOptionList().stream().sorted(Comparator.comparing(V36CodeLibraryOptionDto::getCode).thenComparing(V36CodeLibraryOptionDto::getCreateTime)).toList());
            return digit;
        });
        return queryV36CodeLibraryRespDto;
    }

    private boolean matchSearch(String content, String search){
        if (Objects.nonNull(search)){
            return content != null && content.contains(search);
        }
        return true;
    }

    private void buildDigit(BomsV36CodeLibraryEntity entity,QueryV36CodeLibraryRespDto queryV36CodeLibraryRespDto,Map<Long, V36CodeLibraryDigitDto> digitMap){
        V36CodeLibraryDigitDto dto = new V36CodeLibraryDigitDto();
        dto.setCode(entity.getCode());
        dto.setChineseName(entity.getChineseName().replace(",","|"));
        dto.setRemark(entity.getRemark());
        dto.setDisplayName(entity.getDisplayName().replace(",","|"));
        dto.setSalesFeatureList(entity.getSalesFeatureList().replace(",","|"));
        dto.setCreateUser(entity.getCreateUser());
        dto.setCreateTime(String.valueOf(entity.getCreateTime()));
        dto.setUpdateUser(entity.getUpdateUser());
        dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
        dto.setOptionList(new ArrayList<>());
        queryV36CodeLibraryRespDto.getDigitList().add(dto);
        digitMap.put(entity.getId(),dto);
    }
}
