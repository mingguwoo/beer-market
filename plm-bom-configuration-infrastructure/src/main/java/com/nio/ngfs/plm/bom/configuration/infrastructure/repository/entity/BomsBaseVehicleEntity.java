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
@TableName("boms_base_vehicle")
public class BomsBaseVehicleEntity extends BaseEntity{

    private String baseVehicleId;

    private String modelCode;

    private String modelYear;

    private String regionOptionCode;

    private String driveHand;

    private String salesVersion;

    private String maturity;

    private String status;


}
