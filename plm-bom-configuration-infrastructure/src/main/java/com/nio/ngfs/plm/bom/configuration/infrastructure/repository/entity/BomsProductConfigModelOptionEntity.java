package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * Product Config车型的Option表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-08-09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_product_config_model_option")
public class BomsProductConfigModelOptionEntity extends BaseEntity {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * Option Code
     */
    private String optionCode;

    /**
     * Feature Code
     */
    private String featureCode;

    @Override
    public String toString() {
        return "BomsProductConfigModelOption{" +
                "id = " + getId() +
                ", modelCode = " + modelCode +
                ", optionCode = " + optionCode +
                ", featureCode = " + featureCode +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
