package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.assemble;

import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.QueryV36CodeLibraryChangeLogRespDto;
import org.springframework.beans.BeanUtils;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public class V36CodeLibraryChangeLogAssembler {

    public static QueryV36CodeLibraryChangeLogRespDto assemble (BomsV36CodeLibraryChangeLogEntity entity){
        QueryV36CodeLibraryChangeLogRespDto dto = new QueryV36CodeLibraryChangeLogRespDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setUpdateTime(DateUtils.dateTimeStr(entity.getUpdateTime()));
        return dto;
    }
}
