package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 配置管理Model Year配置表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-08-09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_model_year_config")
public class BomsModelYearConfigEntity extends BaseEntity {

    /**
     * Model
     */
    private String model;

    /**
     * Model Year
     */
    private String modelYear;

    /**
     * OXO是否Release，取值Yes、No
     */
    private String oxoRelease;

    @Override
    public String toString() {
        return "BomsModelYearConfig{" +
            "id = " + getId() +
            ", model = " + model +
            ", modelYear = " + modelYear +
            ", oxoRelease = " + oxoRelease +
            ", createUser = " + getCreateUser() +
            ", updateUser = " + getUpdateUser() +
            ", createTime = " + getCreateTime() +
            ", updateTime = " + getUpdateTime() +
            ", delFlag = " + getDelFlag() +
        "}";
    }
}
