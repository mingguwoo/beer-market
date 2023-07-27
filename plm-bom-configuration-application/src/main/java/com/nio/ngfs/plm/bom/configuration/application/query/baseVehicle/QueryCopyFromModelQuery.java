package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.assemble.BaseVehicleAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.common.BaseVehicleQueryUtil;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.QueryCopyFromModelsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.QueryCopyFromModelRespDto;
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

    private final BomsBasicVehicleDao bomsBasicVehicleDao;
    private final BaseVehicleQueryUtil baseVehicleQueryUtil;

    public QueryCopyFromModelRespDto execute (QueryCopyFromModelsQry qry){
        QueryCopyFromModelRespDto res = new QueryCopyFromModelRespDto();
        List<BomsBasicVehicleEntity> entityList =  bomsBasicVehicleDao.queryCopyFromModel(qry.getModelCode());
        //转换为Dto
        List<BaseVehicleRespDto> dtoList = entityList.stream().map(entity->{
            BaseVehicleRespDto dto = new BaseVehicleRespDto();
            BeanUtils.copyProperties(entity,dto);
            return dto;
        }).collect(Collectors.toList());
        //补全中英文信息并返回结果
        res.setBaseVehicleList(baseVehicleQueryUtil.completeBaseVehicle(dtoList));
        return res;
    }
}
