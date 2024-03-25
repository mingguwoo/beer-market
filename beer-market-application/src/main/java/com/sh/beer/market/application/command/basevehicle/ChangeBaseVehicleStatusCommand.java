package com.sh.beer.market.application.command.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/26
 */
@Component
@RequiredArgsConstructor
public class ChangeBaseVehicleStatusCommand {

    /*private final BaseVehicleRepository baseVehicleRepository;
    private final BaseVehicleDomainService baseVehicleDomainService;

    public ChangeBaseVehicleStatusRespDto execute(ChangeBaseVehicleStatusCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.changeBaseVehicleStatus(cmd);
        baseVehicleRepository.save(baseVehicleAggr);
        return new ChangeBaseVehicleStatusRespDto();
    }*/
}
