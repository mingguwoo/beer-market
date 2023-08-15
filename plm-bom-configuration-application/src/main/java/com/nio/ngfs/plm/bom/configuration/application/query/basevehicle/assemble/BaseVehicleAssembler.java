package com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.assemble;

import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.BaseVehicleRespDto;
import org.springframework.beans.BeanUtils;

/**
 * @author bill.wang
 * @date 2023/7/24
 */
public class BaseVehicleAssembler {

    public static BaseVehicleRespDto assemble (BomsBaseVehicleEntity entity) {
        BaseVehicleRespDto dto = new BaseVehicleRespDto();
        BeanUtils.copyProperties(entity,dto);
        dto.setCreateTime(DateUtils.dateTimeStr(entity.getCreateTime()));
        dto.setUpdateTime(DateUtils.dateTimeStr(entity.getUpdateTime()));
        return dto;
    }
}
