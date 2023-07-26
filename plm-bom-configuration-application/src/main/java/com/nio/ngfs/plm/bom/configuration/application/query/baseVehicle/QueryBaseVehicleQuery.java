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
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.QueryBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
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
public class QueryBaseVehicleQuery extends AbstractQuery<QueryBaseVehicleQry, QueryBaseVehicleRespDto> {

    private final BomsBasicVehicleDao bomsBasicVehicleDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected void validate(QueryBaseVehicleQry queryBaseVehicleQry) {
        if (queryBaseVehicleQry.getStatus() != null && StatusEnum.getByStatus(queryBaseVehicleQry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_STATUS_INVALID);
        }
    }

    @Override
    public QueryBaseVehicleRespDto executeQuery(QueryBaseVehicleQry qry) {
        QueryBaseVehicleRespDto res = new QueryBaseVehicleRespDto();
        List<BomsBasicVehicleEntity> entityList = bomsBasicVehicleDao.queryAll();
        List<BaseVehicleRespDto> dtoList = LambdaUtil.map(entityList, BaseVehicleAssembler::assemble);
        List<BaseVehicleRespDto> filteredDto = filter(dtoList, qry);
        //调取featureDomainDao查询region,drive hand, sales version所有选项,再根据featureCode去筛选
        res.setBaseVehicleRespDtoList(completeBaseVehicle(filteredDto));
        res.setCount(filteredDto.size());
        return res;
    }

    private List<BaseVehicleRespDto> filter(List<BaseVehicleRespDto> dtoList, QueryBaseVehicleQry qry){
        dtoList = dtoList.stream()
                .filter(dto-> StringUtils.isBlank(qry.getModelCode()) || Objects.equals(qry.getModelCode(),dto.getModelCode()))
                .filter(dto->CollectionUtils.isNotEmpty(qry.getModelYear()) || qry.getModelYear().contains(dto.getModelYear()))
                .filter(dto-> StringUtils.isBlank(qry.getStatus()) || Objects.equals(qry.getStatus(),dto.getStatus()))
                .filter(dto-> StringUtils.isBlank(qry.getSalesVersion()) || Objects.equals(qry.getSalesVersion(),dto.getSalesVersion()))
                .filter(dto-> StringUtils.isBlank(qry.getRegionOptionCode()) || Objects.equals(qry.getRegionOptionCode(),dto.getRegionOptionCode()))
                .filter(dto-> StringUtils.isBlank(qry.getDriveHand()) || Objects.equals(qry.getDriveHand(),dto.getDriveHand())).toList();
        return dtoList;
    }
    private List<BaseVehicleRespDto> completeBaseVehicle(List<BaseVehicleRespDto> filteredDto){
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<BomsFeatureLibraryEntity> featureList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        Map<String,BomsFeatureLibraryEntity> codeMap = featureList.stream().collect(Collectors.toMap(BomsFeatureLibraryEntity::getFeatureCode, Function.identity()));
        List<BaseVehicleRespDto> res = filteredDto.stream().map(dto-> {
            BaseVehicleRespDto responseDto = new BaseVehicleRespDto();
            if (!Objects.isNull(codeMap.get(dto.getRegionOptionCode()))) {
                responseDto.setRegionCn(codeMap.get(dto.getRegionOptionCode()).getChineseName());
                responseDto.setRegionEn(codeMap.get(dto.getRegionOptionCode()).getDisplayName());
            }
            if (!Objects.isNull(codeMap.get(dto.getDriveHand()))) {
                responseDto.setDriveCn(codeMap.get(dto.getDriveHand()).getChineseName());
                responseDto.setDriveEn(codeMap.get(dto.getDriveHand()).getDisplayName());
            }
            if (!Objects.isNull(codeMap.get(dto.getSalesVersion()))) {
            responseDto.setSalesVersionCn(codeMap.get(dto.getSalesVersion()).getChineseName());
            responseDto.setSalesVersionEn(codeMap.get(dto.getSalesVersion()).getDisplayName());
            }
            return responseDto;
        }).collect(Collectors.toList());
        return res;
    }

}
