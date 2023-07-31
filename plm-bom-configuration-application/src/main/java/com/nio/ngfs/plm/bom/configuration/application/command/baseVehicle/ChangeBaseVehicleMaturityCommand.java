package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.ChangeBaseVehicleMaturityCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.ChangeBaseVehicleMaturityRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bill.wang
 * @date 2023/7/31
 */
@Component
@RequiredArgsConstructor
public class ChangeBaseVehicleMaturityCommand {
    private final BaseVehicleDomainService baseVehicleDomainService;
    private final BaseVehicleRepository baseVehicleRepository;

    public ChangeBaseVehicleMaturityRespDto execute (ChangeBaseVehicleMaturityCmd cmd){
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleAggr.changeMaturity(cmd);
        List<BaseVehicleAggr> baseVehicleAggrList = baseVehicleRepository.queryByModelCodeAndModelYear(cmd.getModelCode(),cmd.getModelYear());
        baseVehicleAggrList.stream().map(aggr->{
            aggr.setMaturity(cmd.getMaturity());
            return aggr;
        }).collect(Collectors.toList());
        baseVehicleRepository.save(baseVehicleAggr);
        baseVehicleRepository.batchSave(baseVehicleAggrList);
        return new ChangeBaseVehicleMaturityRespDto();
    }
}
