package com.nio.ngfs.plm.bom.configuration.application.command.basevehicle;

import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.EditBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.EditBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/7/20
 */
@Component
@RequiredArgsConstructor
public class EditBaseVehicleCommand {

    private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleRepository baseVehicleRepository;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;
    private final BaseVehicleApplicationService baseVehicleApplicationService;

    @Transactional(rollbackFor = Exception.class)
    public EditBaseVehicleRespDto execute(EditBaseVehicleCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.editBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleRepository.save(baseVehicleAggr);
        //获取该baseVehicle下所有点
        List<OxoOptionPackageAggr> oxoOptionPackageAggrs = oxoOptionPackageRepository.queryByBaseVehicleId(baseVehicleAggr.getId());
        //获取和region,salesVersion,driveHand，modelCode有关的所有行信息，用于筛选
        List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows = baseVehicleApplicationService.queryRegionSalesDrivePoints(cmd.getModelCode());
        //筛选得到相关的打点信息(region,salesVersion,driveHand)
        List<OxoOptionPackageAggr> newPoints = baseVehicleApplicationService.EditBaseVehicleFilter(baseVehicleAggr, oxoOptionPackageAggrs,driveHandRegionSalesVersionRows);
        oxoOptionPackageRepository.saveOrUpdatebatch(newPoints);
        return new EditBaseVehicleRespDto();
    }

}
