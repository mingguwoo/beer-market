package com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.service.impl;

import com.nio.ngfs.plm.bom.configuration.application.query.basevehicle.service.BaseVehicleQueryService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.BaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
@Component
@RequiredArgsConstructor
public class BaseVehicleQueryServiceImpl implements BaseVehicleQueryService {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public List<BaseVehicleRespDto> completeBaseVehicle(List<BaseVehicleRespDto> filteredDto) {
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<BomsFeatureLibraryEntity> featureList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        Map<String,BomsFeatureLibraryEntity> codeMap = featureList.stream().collect(Collectors.toMap(BomsFeatureLibraryEntity::getFeatureCode, Function.identity()));
        List<BaseVehicleRespDto> res = filteredDto.stream().map(dto-> {
            BaseVehicleRespDto responseDto = new BaseVehicleRespDto();
            BeanUtils.copyProperties(dto,responseDto);
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