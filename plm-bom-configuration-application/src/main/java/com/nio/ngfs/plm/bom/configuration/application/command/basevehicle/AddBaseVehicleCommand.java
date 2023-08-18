package com.nio.ngfs.plm.bom.configuration.application.command.basevehicle;

import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleFactory;
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

    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleAggr.addBaseVehicle(cmd);
        //获取oxo行id(region,driveHand,salesVersion三个点)
        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(baseVehicleAggr.getModelCode(),baseVehicleAggr.buildCodeList());
        //构建打点用的聚合根
        List<OxoOptionPackageAggr> packages = OxoOptionPackageFactory.createOxoOptionPackageAggrList(oxoFeatureOptionAggrList,baseVehicleAggr);
        baseVehicleApplicationService.addBaseVehicleSaveToDb(baseVehicleAggr,packages,cmd);
        return new AddBaseVehicleRespDto();
    }
}
