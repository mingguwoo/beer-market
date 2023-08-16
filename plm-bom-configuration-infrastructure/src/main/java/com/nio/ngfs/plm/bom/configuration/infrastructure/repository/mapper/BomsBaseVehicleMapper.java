package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public interface BomsBaseVehicleMapper extends BaseMapper<BomsBaseVehicleEntity> {

    Long getLatestBaseVehicle();

    /**
     * 根据id列表查询
     * @param idList id列表
     * @return BomsBaseVehicleEntity列表
     */
    List<BomsBaseVehicleEntity> queryByIdList(@Param("idList") List<Long> idList);

}
