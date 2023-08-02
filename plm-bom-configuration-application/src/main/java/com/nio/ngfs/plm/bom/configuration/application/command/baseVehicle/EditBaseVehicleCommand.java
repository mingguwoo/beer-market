package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.google.common.collect.Maps;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.EditBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.EditBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/7/20
 */
@Component
@RequiredArgsConstructor
public class EditBaseVehicleCommand {

    private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleRepository baseVehicleRepository;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;
    private final FeatureRepository featureRepository;

    @Transactional
    public EditBaseVehicleRespDto execute(EditBaseVehicleCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.editBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleRepository.save(baseVehicleAggr);
        //获取该baseVehicle下所有点
        List<OxoOptionPackageAggr> oxoOptionPackageAggrs = oxoOptionPackageRepository.queryByBaseVehicleId(baseVehicleAggr.getId());
        //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<FeatureAggr> featureList = featureRepository.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(cmd.getModelCode(),featureList.stream().map(feature->feature.getFeatureId().getFeatureCode()).toList());
        //筛选得到相关的打点信息(region,salesVersion,driveHand)
        List<OxoOptionPackageAggr> newPoints = filter(baseVehicleAggr, oxoOptionPackageAggrs,driveHandRegionSalesVersionRows);
        oxoOptionPackageRepository.saveOrUpdatebatch(newPoints);
        return new EditBaseVehicleRespDto();
    }

    List<OxoOptionPackageAggr> filter (BaseVehicleAggr baseVehicleAggr,List<OxoOptionPackageAggr> aggrs, List<OxoFeatureOptionAggr> rows){
        //先筛选所有行，将他们分成三类
        List<Long> regionList = new ArrayList<>();
        List<Long> driveList = new ArrayList<>();
        List<Long> salesList = new ArrayList<>();
        Map<String,Long> pointRecord = new HashMap<>();
        rows.forEach(row->{
            //将行分类
            if (row.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                salesList.add(row.getId());
            }
            if (row.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_REGION_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                regionList.add(row.getId());
            }
            if (row.getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                driveList.add(row.getId());
            }
            pointRecord.put(row.getFeatureCode(),row.getId());
        });
        //筛选，将非相关点筛选掉
        List<OxoOptionPackageAggr> relatedPoints =  aggrs.stream().filter(aggr->{
            return regionList.contains(aggr.getFeatureOptionId()) || driveList.contains(aggr.getFeatureOptionId()) || salesList.contains(aggr.getFeatureOptionId());
        }).collect(Collectors.toList());
        //修改点
        return aggrs.stream().map(point->{
            if (salesList.contains(point.getFeatureOptionId())){
                point.setFeatureOptionId(pointRecord.get(baseVehicleAggr.getSalesVersion()));
            }
            if (driveList.contains(point.getFeatureOptionId())){
                point.setFeatureOptionId(pointRecord.get(baseVehicleAggr.getDriveHand()));
            }
            if (regionList.contains(point.getFeatureOptionId())){
                point.setFeatureOptionId(pointRecord.get(baseVehicleAggr.getRegionOptionCode()));
            }
            return point;
        }).collect(Collectors.toList());
    }
}
