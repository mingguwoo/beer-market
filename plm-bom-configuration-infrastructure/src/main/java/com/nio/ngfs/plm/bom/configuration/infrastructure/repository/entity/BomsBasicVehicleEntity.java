package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("boms_basic_vehicle")
public class BomsBasicVehicleEntity extends BaseEntity{

    private String baseVehicleId;

    private String model;

    private String modelYear;

    private String region;

    @TableField(value = "region_english_name")
    private String regionEn;

    @TableField(value = "region_chinese_name")
    private String regionCn;

    private String driveHand;

    @TableField(value = "drive_hand_english_name")
    private String driveHandEn;

    @TableField(value = "drive_hand_chinese_name")
    private String driveHandCn;

    private String salesVersion;

    @TableField(value = "sales_version_english_name")
    private String salesVersionEn;

    @TableField(value = "sales_version_chinese_name")
    private String salesVersionCn;

    private String maturity;

    private String status;


}
