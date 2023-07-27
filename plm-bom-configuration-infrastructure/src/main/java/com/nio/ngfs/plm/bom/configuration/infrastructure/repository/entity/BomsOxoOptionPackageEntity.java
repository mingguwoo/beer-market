package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * oxo 打点信息表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_option_package")
public class BomsOxoOptionPackageEntity extends BaseEntity {

    /**
     * oxo行id
     */
    private Long featureOptionId;

    /**
     * oxo列id
     */
    private Long baseVehicleId;

    /**
     * Default ●  Unavailable - Available ○
     */
    private String packageCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 品牌
     */
    private String brand;

    @Override
    public String toString() {
        return "BomsOxoOptionPackage{" +
            "id = " + getId() +
            ", featureOptionId = " + featureOptionId +
            ", baseVehicleId = " + baseVehicleId +
            ", packageCode = " + packageCode +
            ", description = " + description +
            ", brand = " + brand +
            ", createTime = " + getCreateTime() +
            ", updateTime = " + getUpdateTime() +
            ", createUser = " + getCreateUser() +
            ", updateUser = " + getUpdateUser() +
            ", delFlag = " + getDelFlag() +
        "}";
    }
}
