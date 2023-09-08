package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.common.OxoQueryUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.DeleteBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.EditBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/8/3
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BaseVehicleApplicationServiceImpl implements BaseVehicleApplicationService {

    private final FeatureRepository featureRepository;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;
    private final OxoFeatureOptionDomainService oxoFeatureOptionDomainService;
    private final BaseVehicleRepository baseVehicleRepository;
    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;
    @Override
    public void EditBaseVehicleFilter(BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> aggrs, List<OxoFeatureOptionAggr> rows,List<OxoOptionPackageAggr> editPoints,EditBaseVehicleCmd cmd) {
        //先筛选所有行，将他们分成三类
        List<Long> regionList = new ArrayList<>();
        List<Long> driveList= new ArrayList<>();
        List<Long> salesList= new ArrayList<>();
        Map<String,Long> rowRecord = new HashMap<>();
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
            rowRecord.put(row.getFeatureCode(),row.getId());
        });
        //确定要更新的点
        aggrs.forEach(aggr->{
            if (salesList.contains(aggr.getFeatureOptionId())){
                //如果是需要修改为空心圈的点
                if (Objects.equals(rowRecord.get(baseVehicleAggr.getSalesVersion()),aggr.getFeatureOptionId())){
                    aggr.setPackageCode("Default");
                }
                else {
                    aggr.setPackageCode("Unavailable");
                }
                editPoints.add(aggr);
            }
            if (driveList.contains(aggr.getFeatureOptionId())){
                if (Objects.equals(rowRecord.get(baseVehicleAggr.getDriveHand()),aggr.getFeatureOptionId())){
                    aggr.setPackageCode("Default");
                }
                else {
                    aggr.setPackageCode("Unavailable");
                }
                editPoints.add(aggr);
            }
            if (regionList.contains(aggr.getFeatureOptionId())){
                if (Objects.equals(rowRecord.get(baseVehicleAggr.getRegionOptionCode()),aggr.getFeatureOptionId())){
                    aggr.setPackageCode("Default");
                }
                else {
                    aggr.setPackageCode("Unavailable");
                }
                editPoints.add(aggr);
            }
        });
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
            //获取和modelYear,region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
            List<OxoFeatureOptionAggr> modelYearDriveHandRegionSalesVersionRows = queryModelYearRegionSalesDrivePoints(cmd.getModelCode());
            //筛选掉重复的打点信息(modelYear,region,salesVersion,driveHand)
            List<OxoOptionPackageAggr> filteredAggrs = oxoFeatureOptionDomainService.filterRepeatCopyfromPoints(oxoOptionPackageAggrs,modelYearDriveHandRegionSalesVersionRows);
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

    private List<OxoFeatureOptionAggr> queryModelYearRegionSalesDrivePoints(String modelCode){
        //获取和model year,region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
        List<String> codeList = Stream.of(ConfigConstants.FEATURE_CODE_AF00,ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
        List<FeatureAggr> featureList = featureRepository.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
        List<OxoFeatureOptionAggr> modelYeardriveHandRegionSalesVersionRows = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode,featureList.stream().map(feature->feature.getFeatureId().getFeatureCode()).toList());
        return modelYeardriveHandRegionSalesVersionRows;
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void addBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> packages, AddBaseVehicleCmd cmd) {
        //base vehicle增加记录
        baseVehicleRepository.save(baseVehicleAggr);
        //oxo打点
        packages = packages.stream().map(point->{
            point.setBaseVehicleId(baseVehicleAggr.getId());
            return point;
        }).toList();
        oxoOptionPackageRepository.inserOxoOptionPackagesByOxoOptionPackages(packages);
        //copyFrom打点
        addCopyFromPoints(cmd,baseVehicleAggr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBaseVehicleSaveToDb(BaseVehicleAggr baseVehicleAggr) {
        //删除base vehicle记录
        baseVehicleRepository.removeById(baseVehicleAggr.getId());
        //删除oxo中对应打点
        oxoOptionPackageRepository.removeByBaseVehicleIds(baseVehicleAggr.getId());
    }

    @Override
    public void editBaseVehicleAndOxo(BaseVehicleAggr baseVehicleAggr, EditBaseVehicleCmd cmd) {
        //记录option的display name与option code的关系
        //获取该baseVehicle下所有点
        List<OxoOptionPackageAggr> oxoOptionPackageAggrs = oxoOptionPackageRepository.queryByBaseVehicleId(baseVehicleAggr.getId());
        //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
        List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = queryRegionSalesDrivePoints(cmd.getModelCode());
        //筛选得到相关的打点信息(region,salesVersion,driveHand)
        List<OxoOptionPackageAggr> editPoints = new ArrayList<>();
        EditBaseVehicleFilter(baseVehicleAggr, oxoOptionPackageAggrs,driveHandRegionSalesVersionRows,editPoints,cmd);
        editBaseVehicleAndOxoSaveToDb(baseVehicleAggr,editPoints);
    }

    @Override
    public void checkBaseVehicleReleased(DeleteBaseVehicleCmd cmd) {
        List<OxoVersionSnapshotAggr> oxoSnapshotList = oxoVersionSnapshotRepository.queryBomsOxoVersionSnapshotsByModel(cmd.getModelCode());
        if (oxoSnapshotList.isEmpty()){
            return;
        }
        BaseVehicleAggr baseVehicleAggr = baseVehicleRepository.queryBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        if (Objects.isNull(baseVehicleAggr)|| !Objects.equals(cmd.getModelCode(),baseVehicleAggr.getModelCode())){
            throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_NOT_EXISTS);
        }
        if (CollectionUtils.isNotEmpty(oxoSnapshotList)){
            //先筛选掉在baseVehicle之前发布的oxo版本
            oxoSnapshotList.stream().filter(aggr->aggr.getUpdateTime().after(baseVehicleAggr.getUpdateTime())).toList().forEach(aggr->{
                OxoListRespDto oxoListRespDto = OxoQueryUtil.resolveSnapShot(aggr.getOxoSnapshot());
                oxoListRespDto.getOxoHeadResps().forEach(head->{
                    head.getRegionInfos().forEach(region->{
                        //如果region一样，就判断drive hand
                        if (Objects.equals(region.getRegionCode(),baseVehicleAggr.getRegionOptionCode())){
                            region.getDriveHands().forEach(driveHand->{
                                //如果drive hand一样，就判断sales version
                                if (Objects.equals(driveHand.getDriveHandCode(),baseVehicleAggr.getDriveHand())){
                                    driveHand.getSalesVersionInfos().forEach(salesVersion->{
                                        //如果sales version也一样，就报错
                                        if (Objects.equals(salesVersion.getSalesCode(),baseVehicleAggr.getSalesVersion())){
                                            //需要预防之前发布过同样的base vehicle，发布后但删掉了，现在又建了个新的又要删的情况。需要判断发布的版本
                                            if (aggr.getUpdateTime().after(baseVehicleAggr.getUpdateTime())){
                                                throw new BusinessException(ConfigErrorCode.BASE_VEHICLE_ALREADY_RELEASED);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                });
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    void editBaseVehicleAndOxoSaveToDb(BaseVehicleAggr baseVehicleAggr, List<OxoOptionPackageAggr> editPoints){
        baseVehicleRepository.save(baseVehicleAggr);
        oxoOptionPackageRepository.saveOrUpdatebatch(editPoints);
    }

    public String queryModelYearOptionCodeByDisplayName(String modelYear){
        List<FeatureAggr> featureAggrList = featureRepository.queryByParentFeatureCodeAndType(ConfigConstants.FEATURE_CODE_AF00,FeatureTypeEnum.OPTION.getType());
        featureAggrList =  featureAggrList.stream().filter(aggr->Objects.equals(aggr.getDisplayName(),modelYear)).toList();
        return featureAggrList.get(CommonConstants.INT_ZERO).getFeatureCode();
    }
}
