package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoOptionPackageDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.AddBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class AddBaseVehicleCommand {

    private final BaseVehicleRepository baseVehicleRepository;
    private final BaseVehicleDomainService baseVehicleDomainService;
    private final OxoOptionPackageDomainService oxoOptionPackageDomainService;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;
    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final OxoFeatureOptionDomainService oxoFeatureOptionDomainService;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;


    @Transactional( rollbackFor = Exception.class)
    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleAggr.addBaseVehicle(cmd);
        baseVehicleRepository.save(baseVehicleAggr);
        //获取oxo行id(region,driveHand,salesVersion三个点)
        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(baseVehicleAggr.getModelCode(),baseVehicleAggr.buildCodeList());
        //构建打点用的聚合根
        List<OxoOptionPackageAggr> packages = OxoOptionPackageFactory.createOxoOptionPackageAggrList(oxoFeatureOptionAggrList,baseVehicleAggr);
        //oxo打点
        oxoOptionPackageRepository.inserOxoOptionPackagesByOxoOptionPackages(packages);
        //copyFrom
        if (cmd.isCopyFrom()){
            //获取要Copy的Model的所有打点信息,要注意这里的baseVehicleId对应的是BasieVehicle表里的id字段，不是baseVehicleId字段
            List<OxoOptionPackageAggr> oxoOptionPackageAggrs = oxoOptionPackageRepository.queryByBaseVehicleId(cmd.getCopyModelId());
            //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
            List<String> codeList = Stream.of(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE,ConfigConstants.BASE_VEHICLE_REGION_FEATURE,ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE).collect(Collectors.toList());
            List<FeatureAggr> featureList = featureRepository.queryByParentFeatureCodeListAndType(codeList, FeatureTypeEnum.OPTION.getType());
            List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(cmd.getModelCode(),featureList.stream().map(feature->feature.getFeatureId().getFeatureCode()).toList());
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
        return new AddBaseVehicleRespDto();
    }
}
