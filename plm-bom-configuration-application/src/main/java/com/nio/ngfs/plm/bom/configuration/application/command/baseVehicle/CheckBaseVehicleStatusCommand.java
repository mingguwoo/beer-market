package com.nio.ngfs.plm.bom.configuration.application.command.baseVehicle;

import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.CheckBaseVehicleStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.CheckBaseVehicleStatusRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/7/21
 */

@Component
@RequiredArgsConstructor
public class CheckBaseVehicleStatusCommand {

    private OxoDomainService oxoDomainService;

    public CheckBaseVehicleStatusRespDto execute(CheckBaseVehicleStatusCmd cmd) {

        //查询oxo中该版本是否已发布

        return new CheckBaseVehicleStatusRespDto();
    }
}
