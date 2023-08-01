package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.EditBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.EditBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/20
 */
@Component
@RequiredArgsConstructor
public class EditBaseVehicleCommand {

    private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleRepository baseVehicleRepository;

    public EditBaseVehicleRespDto execute(EditBaseVehicleCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.editBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleRepository.save(baseVehicleAggr);
        return new EditBaseVehicleRespDto();
    }
}
