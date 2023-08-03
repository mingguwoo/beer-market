package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
@Component
@RequiredArgsConstructor
public class BaseVehicleApplicationServiceImpl implements BaseVehicleApplicationService {

    private final FeatureRepository featureRepository;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;
    private final OxoFeatureOptionDomainService oxoFeatureOptionDomainService;
    private List<Long> regionList = new ArrayList<>();
    private List<Long> driveList= new ArrayList<>();
    private List<Long> salesList= new ArrayList<>();

    @Override
    public List<OxoOptionPackageAggr> EditBaseVehicleFilter(BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> aggrs, List<OxoFeatureOptionAggr> rows) {
        //先筛选所有行，将他们分成三类
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

    @Override
    public List<OxoFeatureOptionAggr> queryRegionSalesDrivePoints(String modelCode) {
        //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
        List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<FeatureAggr> featureList = featureRepository.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode,featureList.stream().map(feature->feature.getFeatureId().getFeatureCode()).toList());
        return driveHandRegionSalesVersionRows;
    }

    @Override
    public void addCopyFromPoints(AddBaseVehicleCmd cmd, BaseVehicleAggr baseVehicleAggr) {
        if (cmd.isCopyFrom()){
            //获取要Copy的Model的所有打点信息,要注意这里的baseVehicleId对应的是BasieVehicle表里的id字段，不是baseVehicleId字段
            List<OxoOptionPackageAggr> oxoOptionPackageAggrs = oxoOptionPackageRepository.queryByBaseVehicleId(cmd.getCopyModelId());
            //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
            List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = queryRegionSalesDrivePoints(cmd.getModelCode());
            //筛选掉重复的打点信息(region,salesVersion,driveHand)
            List<OxoOptionPackageAggr> filteredAggrs = oxoFeatureOptionDomainService.filter(oxoOptionPackageAggrs,driveHandRegionSalesVersionRows);
            //oxo打点
            oxoOptionPackageRepository.inserOxoOptionPackagesByOxoOptionPackages(filteredAggrs.stream().map(aggr->{
                aggr.setBaseVehicleId(baseVehicleAggr.getId());
                //将原先id清除
                aggr.setId(null);
                aggr.setBrand(ConfigConstants.brandName.get());
                return aggr;
            }).toList());
        }

    }
}
