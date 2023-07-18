package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author bill.wang
 * @date 2023/7/18
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_basic_vehicle")
public class BomsBasicVehicleEntity extends BaseEntity{

    private String baseVehicleId;

    private String model;

    private String modelYear;

    private String region;

    private String regionEn;

    private String regionCn;

    private String driveHand;

    private String driveHandEn;

    private String driveHandCn;

    private String salesVersion;

    private String salesVersionEn;

    private String salesVersionCn;

    private String owner;

    private String maturity;

    private String status;

}
