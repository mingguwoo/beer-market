package com.sh.beer.market.application.command.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/20
 */
@Component
@RequiredArgsConstructor
public class EditBaseVehicleCommand {

    /*private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleApplicationService baseVehicleApplicationService;

    public EditBaseVehicleRespDto execute(EditBaseVehicleCmd cmd) {
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.editBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleApplicationService.editBaseVehicleAndOxo(baseVehicleAggr,cmd);
        return new EditBaseVehicleRespDto();
    }*/

}
