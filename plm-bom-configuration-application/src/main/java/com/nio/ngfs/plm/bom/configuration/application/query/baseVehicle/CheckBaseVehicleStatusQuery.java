package com.nio.ngfs.plm.bom.configuration.application.query.baseVehicle;

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
public class CheckBaseVehicleStatusQuery {

//    private final BomsOxoVersionSnapShotDao oxoVersionSnapShotDao;

    public CheckBaseVehicleStatusRespDto execute(CheckBaseVehicleStatusCmd cmd) {

        //查询oxo中该版本是否已发布
        CheckBaseVehicleStatusRespDto checkBaseVehicleStatusRespDto = new CheckBaseVehicleStatusRespDto();
        //1是发布，0是没发布
//        checkBaseVehicleStatusRespDto.setReleased(oxoVersionSnapShotDao.checkBaseVehicleStatus(cmd.getModelCode()));
        return checkBaseVehicleStatusRespDto;
    }
}
