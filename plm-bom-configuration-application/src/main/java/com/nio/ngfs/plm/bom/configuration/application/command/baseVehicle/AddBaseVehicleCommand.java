package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
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

    private final BaseVehicleRepository baseVehicleRepository;
    private final BaseVehicleDomainService baseVehicleDomainService;

    public AddBaseVehicleRespDto execute(AddBaseVehicleCmd cmd){
        BaseVehicleAggr baseVehicleAggr = BaseVehicleFactory.createBaseVehicle(cmd);
        baseVehicleDomainService.checkBaseVehicleUnique(baseVehicleAggr);
        baseVehicleAggr.addBaseVehicle(cmd);
        baseVehicleRepository.save(baseVehicleAggr);
        //oxo打点

        //copyFrom
        if (cmd.isCopyFrom()){
            //查询Oxo表，获取其余打点信息，存入库中
        }
        return new AddBaseVehicleRespDto();
    }
}
