package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle.assemble.BaseVehicleAssembler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBasicVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.QueryBaseVehicleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.QueryBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/7/24
 */
@Component
@RequiredArgsConstructor
public class QueryBaseVehicleQuery extends AbstractQuery<QueryBaseVehicleQry, List<QueryBaseVehicleRespDto>> {

    private final BomsBasicVehicleDao bomsBasicVehicleDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

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
        //调取featureDomainDao查询region,drive hand, sales version所有选项,再根据查询条件去筛选
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<BomsFeatureLibraryEntity> featureList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.FEATURE.getType());
        Map<String,BomsFeatureLibraryEntity> codeMap = featureList.stream().collect(Collectors.toMap(BomsFeatureLibraryEntity::getFeatureCode, Function.identity()));
        List<QueryBaseVehicleRespDto> res = filteredDto.stream().map(dto->{
            dto.setRegionCn(codeMap.get(dto.getRegionOptionCode()).getChineseName());
            dto.setRegionEn(codeMap.get(dto.getRegionOptionCode()).getDisplayName());
            dto.setDriveCn(codeMap.get(dto.getDriveHand()).getChineseName());
            dto.setDriveEn(codeMap.get(dto.getDriveHand()).getDisplayName());
            dto.setSalesVersionCn(codeMap.get(dto.getSalesVersion()).getChineseName());
            dto.setSalesVersionEn(codeMap.get(dto.getSalesVersion()).getDisplayName());
            return dto;
        }).collect(Collectors.toList());
        return res;
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
