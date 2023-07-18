package com.nio.ngfs.plm.bom.configuration.application.command.BaseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.AddBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.AddBaseVehicleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class AddBaseVehicleCommand {
    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);//还有status,maturity
        baseVehicleAggr.addBaseVehicle(cmd);

        return null;
    }
}
