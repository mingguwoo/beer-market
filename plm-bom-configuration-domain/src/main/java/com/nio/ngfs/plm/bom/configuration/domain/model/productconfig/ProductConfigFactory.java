package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.AddPcCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public class ProductConfigFactory {

    public static ProductConfigAggr create(AddPcCmd cmd, String pcId, String brand) {
        return ProductConfigAggr.builder()
                .pcId(pcId)
                .model(cmd.getModel())
                .modelYear(cmd.getModelYear())
                .name(cmd.getName())
                .basedOnBaseVehicleId(cmd.getBasedOnBaseVehicleId())
                .oxoVersionSnapshotId(cmd.getOxoVersionSnapshotId())
                .basedOnPcId(cmd.getBasedOnPcId())
                .brand(brand)
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

}
