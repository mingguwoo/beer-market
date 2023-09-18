package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.QueryV36CodeLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.QueryV36CodeLibraryRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class QueryV36CodeLibraryQuery {

    private final BomsV36CodeLibraryDao bomsV36CodeLibraryDao;

    public List<QueryV36CodeLibraryRespDto> execute(QueryV36CodeLibraryQry qry){
        List<BomsV36CodeLibraryEntity> v36CodeLibraryEntityList = bomsV36CodeLibraryDao.queryBySalesFeatureAndName(qry.getSalesFeature(),qry.getName());
        return v36CodeLibraryEntityList.stream().map(entity->{
            QueryV36CodeLibraryRespDto dto = new QueryV36CodeLibraryRespDto();
            dto.setCode(entity.getCode());
            dto.setDisplayName(entity.getDisplayName());
            dto.setChineseName(entity.getChineseName());
            dto.setSalesFeatureList(entity.getSalesFeatureList());
            dto.setRemark(entity.getRemark());
            dto.setCreateUser(entity.getCreateUser());
            dto.setCreateTime(String.valueOf(entity.getCreateTime()));
            dto.setUpdateUser(entity.getUpdateUser());
            dto.setUpdateTime(String.valueOf(entity.getUpdateTime()));
            return dto;
        }).toList();
    }
}
