package com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble;

import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.GetChangeLogListDto;
import org.springframework.beans.BeanUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
public class FeatureChangeLogAssembler {

    public static GetChangeLogListDto assemble(BomsFeatureChangeLogEntity entity) {
        GetChangeLogListDto getChangeLogListDto = new GetChangeLogListDto();
        BeanUtils.copyProperties(entity, getChangeLogListDto);
        getChangeLogListDto.setUpdateTime(DateUtils.dateTimeStr(entity.getUpdateTime()));
        return getChangeLogListDto;
    }

}
