package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
@Data
public class BaseVehicleRespDto implements Dto {

    private String baseVehicleId;

    private String modelCode;

    private String modelYear;

    private String regionOptionCode;

    private String regionEn;

    private String regionCn;

    private String driveHand;

    private String driveEn;

    private String driveCn;

    private String salesVersion;

    private String salesVersionEn;

    private String salesVersionCn;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;

    private String status;
}
