package com.nio.ngfs.plm.bom.configuration.application.command.basevehicle;

import com.nio.ngfs.plm.bom.configuration.application.service.BaseVehicleApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request.DeleteBaseVehicleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.response.DeleteBaseVehicleRespDto;
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
    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final BaseVehicleApplicationService baseVehicleApplicationService;

    public DeleteBaseVehicleRespDto execute(DeleteBaseVehicleCmd cmd) {
        //查询oxo中该版本是否已发布
        oxoVersionSnapshotDomainService.checkBaseVehicleReleased(cmd.getModelCode());
        //删除BaseVehicle
        BaseVehicleAggr baseVehicleAggr = baseVehicleDomainService.getBaseVehicleByBaseVehicleId(cmd.getBaseVehicleId());
        baseVehicleDomainService.checkBaseVehicleExist(baseVehicleAggr);
        baseVehicleApplicationService.deleteBaseVehicleSaveToDb(baseVehicleAggr);
        return new DeleteBaseVehicleRespDto();
    }
}
