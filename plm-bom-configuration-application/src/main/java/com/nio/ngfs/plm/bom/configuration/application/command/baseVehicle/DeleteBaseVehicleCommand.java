package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.DeleteBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.DeleteBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/21
 */
@Component
@RequiredArgsConstructor
public class DeleteBaseVehicleCommand {

    private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleRepository baseVehicleRepository;

    public DeleteBaseVehicleRespDto execute(DeleteBaseVehicleCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleRepository.removeById(baseVehicleAggr.getId());
        return new DeleteBaseVehicleRespDto();
    }
}
