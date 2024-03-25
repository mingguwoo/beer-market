package com.sh.beer.market.application.command.basevehicle;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/21
 */
@Component
@RequiredArgsConstructor
public class DeleteBaseVehicleCommand {

    /*private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleApplicationService baseVehicleApplicationService;

    public DeleteBaseVehicleRespDto execute(DeleteBaseVehicleCmd cmd) {
        baseVehicleApplicationService.checkBaseVehicleReleased(cmd);
        //删除BaseVehicle
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleApplicationService.deleteBaseVehicleSaveToDb(baseVehicleAggr);
        return new DeleteBaseVehicleRespDto();
    }*/
}
