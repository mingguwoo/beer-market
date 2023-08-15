package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 单车PC表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-08-09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_product_config")
public class BomsProductConfigEntity extends BaseEntity {

    /**
     * 单车PC ID
     */
    private String pcId;

    /**
     * Model
     */
    private String modelCode;

    /**
     * Model Year
     */
    private String modelYear;

    /**
     * 单车PC Name
     */
    private String name;

    /**
     * 单车PC Marketing Name
     */
    private String marketingName;

    /**
     * 单车PC描述
     */
    private String description;

    /**
     * Based On Base Vehicle主键ID
     */
    private Long basedOnBaseVehicleId;

    /**
     * Based On Base Vehicle所在的OXO发布版本id
     */
    private Long oxoVersionSnapshotId;

    /**
     * Based On Base Vehicle是否完成初始化勾选，0：否，1：是
     */
    private Integer completeInitSelect;

    /**
     * Based On PC ID
     */
    private String basedOnPcId;

    /**
     * Model的品牌
     */
    private String brand;

    /**
     * Skip Check开关，取值Open、Close
     */
    private String skipCheck;

    @Override
    public String toString() {
        return "BomsProductConfig{" +
                "id = " + getId() +
                ", pcId = " + pcId +
                ", modelCode = " + modelCode +
                ", modelYear = " + modelYear +
                ", name = " + name +
                ", marketingName = " + marketingName +
                ", description = " + description +
                ", basedOnBaseVehicleId = " + basedOnBaseVehicleId +
                ", oxoVersionSnapshotId = " + oxoVersionSnapshotId +
                ", completeInitSelect = " + completeInitSelect +
                ", basedOnPcId = " + basedOnPcId +
                ", brand = " + brand +
                ", skipCheck = " + skipCheck +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
