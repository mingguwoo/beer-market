package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.assemble;

import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.QueryBaseVehicleRespDto;
import org.springframework.beans.BeanUtils;

/**
 * @author bill.wang
 * @date 2023/7/24
 */
public class BaseVehicleAssembler {

    public static QueryBaseVehicleRespDto assemble (BomsBasicVehicleEntity entity) {
        QueryBaseVehicleRespDto dto = new QueryBaseVehicleRespDto();
        BeanUtils.copyProperties(entity,dto);
        dto.setCreateTime(DateUtils.dateTimeStr(entity.getCreateTime()));
        dto.setUpdateTime(DateUtils.dateTimeStr(entity.getUpdateTime()));
        return dto;
    }
}
