package com.sh.beer.market.application.query.basevehicle.service.impl;


import com.sh.beer.market.application.query.basevehicle.service.BaseVehicleQueryService;
import com.sh.beer.market.sdk.dto.basevehicle.response.BaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 * @date 2023/8/3
 */
@Component
@RequiredArgsConstructor
public class BaseVehicleQueryServiceImpl implements BaseVehicleQueryService {

//    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public List<BaseVehicleRespDto> completeBaseVehicle(List<BaseVehicleRespDto> filteredDto) {
        /*List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
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
    }*/
        return null;
    }
}
