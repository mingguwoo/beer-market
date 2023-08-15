package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BomsBaseVehicleMapper extends BaseMapper<BomsBaseVehicleEntity> {

    Long getLatestBaseVehicle();
}
