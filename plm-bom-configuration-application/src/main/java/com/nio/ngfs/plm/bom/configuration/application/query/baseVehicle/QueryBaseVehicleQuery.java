package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.assemble.BaseVehicleAssembler;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.QueryBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.QueryBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author bill.wang
 * @date 2023/7/24
 */
@Component
@RequiredArgsConstructor
public class QueryBaseVehicleQuery extends AbstractQuery<QueryBaseVehicleQry, List<QueryBaseVehicleRespDto>> {

    private final BomsBasicVehicleDao bomsBasicVehicleDao;

    @Override
    protected void validate(QueryBaseVehicleQry queryBaseVehicleQry) {
        if (queryBaseVehicleQry.getStatus() != null && StatusEnum.getByStatus(queryBaseVehicleQry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_STATUS_INVALID);
        }
    }

    @Override
    public List<QueryBaseVehicleRespDto> executeQuery(QueryBaseVehicleQry qry) {
        List<BomsBasicVehicleEntity> entityList = bomsBasicVehicleDao.queryAll();
        List<QueryBaseVehicleRespDto> dtoList = LambdaUtil.map(entityList, BaseVehicleAssembler::assemble);
        List<QueryBaseVehicleRespDto> filteredDto = filter(dtoList, qry);
        //调取featureLibrary接口，查询CN，EN


        return dtoList;
    }

    private List<QueryBaseVehicleRespDto> filter(List<QueryBaseVehicleRespDto> dtoList, QueryBaseVehicleQry qry){
        if (StringUtils.isNotBlank(qry.getModelCode())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getModelCode(),dto.getModelCode())).toList();
        }
        if (StringUtils.isNotBlank(qry.getModelYear())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getModelYear(),dto.getModelYear())).toList();
        }
        if (StringUtils.isNotBlank(qry.getStatus())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getStatus(),dto.getStatus())).toList();
        }
        if (StringUtils.isNotBlank(qry.getSalesVersion())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getSalesVersion(),dto.getSalesVersion())).toList();
        }
        if (StringUtils.isNotBlank(qry.getRegionOptionCode())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getRegionOptionCode(),dto.getRegionOptionCode())).toList();
        }
        if (StringUtils.isNotBlank(qry.getDriveHand())){
            dtoList.stream().filter(dto-> Objects.equals(qry.getDriveHand(),dto.getDriveHand())).toList();
        }
        return dtoList;
    }

}
