package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.assemble.V36CodeLibraryChangeLogAssembler;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryChangeLogDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.QueryV36CodeLibraryChangeLogQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.QueryV36CodeLibraryChangeLogRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
@Component
@RequiredArgsConstructor
public class QueryV36CodeLibraryChangeLogQuery {

    private final BomsV36CodeLibraryChangeLogDao bomsV36CodeLibraryChangeLogDao;

    public List<QueryV36CodeLibraryChangeLogRespDto> execute(QueryV36CodeLibraryChangeLogQry qry){
        List<BomsV36CodeLibraryChangeLogEntity> changeLogEntityList = bomsV36CodeLibraryChangeLogDao.queryByCodeId(qry.getCodeId()).stream().sorted(Comparator.comparing(BomsV36CodeLibraryChangeLogEntity::getUpdateTime)).collect(Collectors.toList());
        Collections.reverse(changeLogEntityList);
        return LambdaUtil.map(changeLogEntityList, V36CodeLibraryChangeLogAssembler::assemble);
    }
}
