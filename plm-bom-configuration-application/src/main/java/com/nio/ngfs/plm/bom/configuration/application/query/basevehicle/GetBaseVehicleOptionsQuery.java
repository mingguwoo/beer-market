package com.nio.ngfs.plm.bom.configuration.application.query.basevehicle;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.GetBaseVehicleOptionsQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.BaseVehicleOptionsRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.GetBaseVehicleOptionsRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Component
@RequiredArgsConstructor
public class GetBaseVehicleOptionsQuery {

    private final BomsOxoFeatureOptionDao bomsOxoFeatureOptionDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    public GetBaseVehicleOptionsRespDto execute(GetBaseVehicleOptionsQry qry) {
        //  获取全部region，drive hand， sales version
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<BomsFeatureLibraryEntity> allFeatures = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        List<String> featureList = allFeatures.stream().map(feature->feature.getFeatureCode()).collect(Collectors.toList());
        //如果有modelCode，说明是新增或者带着modelCode的搜索条件筛选，用modelCode去oxoFeatureOptionDao进行批量查询，看有哪些存在
        if (Objects.nonNull(qry.getModelCode())){
            List<BomsOxoFeatureOptionEntity> options = bomsOxoFeatureOptionDao.getBaseVehicleOptions(featureList,qry.getModelCode());
            List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntities = bomsFeatureLibraryDao.queryByFeatureOptionCodeList((options.stream().map(option-> option.getFeatureCode()).toList()));
            return sortBaseVehicleOptions(bomsFeatureLibraryEntities);
        }
        //如果没有modelCode，说明是全量搜索条件
        else {
            List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntities = allFeatures;
            return sortBaseVehicleOptions(bomsFeatureLibraryEntities);
        }
        }


    /**
     * 将Option Code List分类成Region Option Code, Sales Version, Drive Hand三类并补齐其他信息
     */
    private GetBaseVehicleOptionsRespDto sortBaseVehicleOptions(List<BomsFeatureLibraryEntity> bomsFeatureLibraryEntities) {
        GetBaseVehicleOptionsRespDto ans = new GetBaseVehicleOptionsRespDto();
        ans.setRegionOptionCodeList(new ArrayList<>());
        ans.setDriveHandList(new ArrayList<>());
        ans.setSalesVersionList(new ArrayList<>());
        bomsFeatureLibraryEntities.forEach(entity->{
            //根据开头两个字母进行分类,只有三种：salesVersion/driveHand/regionOptionCode
            if (entity.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(entity.getFeatureCode());
                option.setDescription(entity.getDescription());
                option.setChineseName(entity.getChineseName());
                option.setEnglishName(entity.getDisplayName());
                ans.getSalesVersionList().add(option);
            }
            else if (entity.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(entity.getFeatureCode());
                option.setDescription(entity.getDescription());
                option.setChineseName(entity.getChineseName());
                option.setEnglishName(entity.getDisplayName());
                ans.getDriveHandList().add(option);
            }
            else {
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(entity.getFeatureCode());
                option.setDescription(entity.getDescription());
                option.setChineseName(entity.getChineseName());
                option.setEnglishName(entity.getDisplayName());
                ans.getRegionOptionCodeList().add(option);            }
        });
        return ans;
    }
}
