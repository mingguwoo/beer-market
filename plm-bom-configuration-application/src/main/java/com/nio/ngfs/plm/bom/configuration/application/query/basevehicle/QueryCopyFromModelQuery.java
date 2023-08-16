package com.nio.ngfs.plm.bom.configuration.application.query.basevehicle;

import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.service.BaseVehicleQueryService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.QueryCopyFromModelsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.BaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Component
@RequiredArgsConstructor
public class QueryCopyFromModelQuery {

    private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BaseVehicleQueryService baseVehicleQueryService;


    public List<BaseVehicleRespDto> execute (QueryCopyFromModelsQry qry){
        List<BomsBaseVehicleEntity> entityList =  bomsBaseVehicleDao.queryCopyFromModel(qry.getModelCode());
        //转换为Dto
        List<BaseVehicleRespDto> dtoList = entityList.stream().map(entity->{
            BaseVehicleRespDto dto = new BaseVehicleRespDto();
            BeanUtils.copyProperties(entity,dto);
            return dto;
        }).collect(Collectors.toList());
        //补全中英文信息并返回结果
        return baseVehicleQueryService.completeBaseVehicle(dtoList);
    }
}