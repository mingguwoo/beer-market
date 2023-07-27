package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.GetBaseVehicleOptionsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.GetBaseVehicleOptionsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Component
@RequiredArgsConstructor
public class GetBaseVehicleOptionsQuery {

    private BomsOxoFeatureOptionDao bomsOxoFeatureOptionDao;
    private BomsFeatureLibraryDao bomsFeatureLibraryDao;

    public GetBaseVehicleOptionsRespDto execute(GetBaseVehicleOptionsQry qry) {
        //  获取全部region，drive hand， sales version
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<BomsFeatureLibraryEntity> allFeatures = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        List<String> featureList = allFeatures.stream().map(feature->feature.getFeatureCode()).collect(Collectors.toList());
        //加上modelCode去oxoFeatureOptionDao进行批量查询，看有哪些存在
        List<BomsOxoFeatureOptionEntity> filteredList = bomsOxoFeatureOptionDao.getBaseVehicleOptions(featureList,qry);
        return buildResponse(filteredList);
    }

    private GetBaseVehicleOptionsRespDto buildResponse (List<BomsOxoFeatureOptionEntity> filteredList){
        GetBaseVehicleOptionsRespDto getBaseVehicleOptionsRespDto = new GetBaseVehicleOptionsRespDto();
        Set<String> regionOptionCode = new HashSet<>();
        Set<String> driveHand = new HashSet<>();
        Set<String> salesVersion = new HashSet<>();
        filteredList.forEach(entity->{
            //根据开头两个字母进行分类
            if (entity.getFeatureCode().substring(CommonConstants.INT_ZERO).equals(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE.substring(CommonConstants.INT_ZERO))){
                salesVersion.add(entity.getFeatureCode());
            }
            else if (entity.getFeatureCode().substring(CommonConstants.INT_ZERO).equals(ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE.substring(CommonConstants.INT_ZERO))){
                driveHand.add(entity.getFeatureCode());
            }
            else {
                regionOptionCode.add(entity.getFeatureCode());
            }
        });
        getBaseVehicleOptionsRespDto.setRegionOptionCode(new ArrayList<>(regionOptionCode));
        getBaseVehicleOptionsRespDto.setDriveHand(new ArrayList<>(driveHand));
        getBaseVehicleOptionsRespDto.setSalesVersion(new ArrayList<>(salesVersion));
        return getBaseVehicleOptionsRespDto;
    }
}
