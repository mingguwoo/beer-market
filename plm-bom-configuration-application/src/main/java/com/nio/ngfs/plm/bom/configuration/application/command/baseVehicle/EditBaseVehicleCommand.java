package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
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
        baseVehicleAggr.editBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleRepository.save(baseVehicleAggr);
        //更新oxo表并打点

        return new EditBaseVehicleRespDto();
    }
}
