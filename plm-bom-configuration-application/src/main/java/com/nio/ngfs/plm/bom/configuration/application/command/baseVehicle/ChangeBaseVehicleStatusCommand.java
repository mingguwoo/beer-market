package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ChangeBaseVehicleStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.ChangeBaseVehicleStatusRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/26
 */
@Component
@RequiredArgsConstructor
public class ChangeBaseVehicleStatusCommand {

    private final BaseVehicleRepository baseVehicleRepository;
    private final BaseVehicleDomainService baseVehicleDomainService;

    public ChangeBaseVehicleStatusRespDto execute(ChangeBaseVehicleStatusCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.changeBaseVehicleStatus(cmd);
        baseVehicleRepository.save(baseVehicleAggr);
        return new ChangeBaseVehicleStatusRespDto();
    }
}
