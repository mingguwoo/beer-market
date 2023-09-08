package com.nio.ngfs.plm.bom.configuration.application.command.basevehicle;

import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.AddBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class AddBaseVehicleCommand {

    private final BaseVehicleDomainService baseVehicleDomainService;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final BaseVehicleApplicationService baseVehicleApplicationService;
    private final FeatureRepository featureRepository;

    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleAggr.addBaseVehicle(cmd);
        List<String> childList = ConfigConstants.BASE_VEHICLE_FEATURE_CODE_LIST;
        childList.add(ConfigConstants.FEATURE_CODE_AF00);
        //后续新增需求，model Year也要考虑。因原有constants已被其他模块复用，因此在这直接添加
        List<String> codeList = featureRepository.queryByParentFeatureCodeListAndType(childList, FeatureTypeEnum.OPTION.getType()).stream().map(option->option.getFeatureCode()).toList();
        //获取oxo行id(model year,region,driveHand,salesVersion相关行)
        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(baseVehicleAggr.getModelCode(),codeList);
        //获取model year对应的feature code
        String modelYearCode = baseVehicleApplicationService.queryModelYearOptionCodeByDisplayName(cmd.getModelYear());
        //构建打点用的聚合根
        List<OxoOptionPackageAggr> packages = OxoOptionPackageFactory.createOxoOptionPackageAggrList(oxoFeatureOptionAggrList,baseVehicleAggr,modelYearCode);
        baseVehicleApplicationService.addBaseVehicleSaveToDb(baseVehicleAggr,packages,cmd);
        return new AddBaseVehicleRespDto();
    }
}
