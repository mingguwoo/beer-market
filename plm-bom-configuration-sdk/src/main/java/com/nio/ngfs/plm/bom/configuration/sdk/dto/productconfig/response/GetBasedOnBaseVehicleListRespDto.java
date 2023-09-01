package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Data
public class GetBasedOnBaseVehicleListRespDto implements Dto {

    private String model;

    private String modelYear;

    private List<BasedOnBaseVehicleDto> baseVehicleList;

    @Data
    public static class BasedOnBaseVehicleDto {

        private Long id;

        private String baseVehicleId;

        private String regionCode;

        private String regionCn;

        private String driveHandCode;

        private String driveHandCn;

        private String salesVersionCode;

        private String salesVersionCn;

        private Long oxoVersionSnapshotId;

    }

}
