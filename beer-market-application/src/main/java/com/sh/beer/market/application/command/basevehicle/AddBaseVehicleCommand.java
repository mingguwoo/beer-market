package com.sh.beer.market.application.command.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class AddBaseVehicleCommand {

    /*private final BaseVehicleDomainService baseVehicleDomainService;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final BaseVehicleApplicationService baseVehicleApplicationService;
    private final FeatureRepository featureRepository;

    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleAggr.addBaseVehicle(cmd);
        List<String> childList = ConfigConstants.BASE_VEHICLE_FEATURE_CODE_LIST;
        //后续新增需求，model Year也要考虑。因原有constants已被其他模块复用，因此在这直接添加
        childList.add(ConfigConstants.FEATURE_CODE_AF00);
        List<String> codeList = featureRepository.queryByParentFeatureCodeListAndType(childList, FeatureTypeEnum.OPTION.getType()).stream().map(option->option.getFeatureCode()).toList();
        //获取oxo行id(model year,region,driveHand,salesVersion相关行)，还要过滤一下，考虑到softDelete的软删除
        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(baseVehicleAggr.getModelCode(),codeList).stream().filter(aggr-> Objects.equals(aggr.getSoftDelete(), CommonConstants.INT_ZERO)).toList();
        //获取model year对应的feature code
        String modelYearCode = baseVehicleApplicationService.queryModelYearOptionCodeByDisplayName(cmd.getModelYear());
        //构建打点用的聚合根
        List<OxoOptionPackageAggr> packages = OxoOptionPackageFactory.createOxoOptionPackageAggrList(oxoFeatureOptionAggrList,baseVehicleAggr,modelYearCode);
        baseVehicleApplicationService.addBaseVehicleSaveToDb(baseVehicleAggr,packages,cmd);
        return new AddBaseVehicleRespDto();
    }*/
}
