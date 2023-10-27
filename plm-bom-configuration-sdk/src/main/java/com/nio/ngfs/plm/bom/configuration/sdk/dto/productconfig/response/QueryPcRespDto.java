package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Builder;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/8/16
 */
@Data
public class QueryPcRespDto implements Dto {

    private String pcId;

    private String model;

    private String modelYear;

    private String name;

    private Integer completeInitSelect;

    private BasedOnBaseVehicleDto basedOnBaseVehicle;

    private String basedOnPcId;

    private String basedOnPcName;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;

    @Data
    @Builder
    public static class BasedOnBaseVehicleDto {

        private String baseVehicleId;

        private String regionCode;

        private String regionCn;

        private String driveHandCode;

        private String driveHandCn;

        private String salesVersionCode;

        private String salesVersionCn;

        @Override
        public String toString() {
            return regionCode + "/" + driveHandCode + "/" + salesVersionCode;
        }

    }

}
