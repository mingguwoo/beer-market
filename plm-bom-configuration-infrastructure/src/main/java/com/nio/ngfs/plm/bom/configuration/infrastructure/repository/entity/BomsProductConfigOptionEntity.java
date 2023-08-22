package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 单车PC的Option配置表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-08-09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_product_config_option")
public class BomsProductConfigOptionEntity extends BaseEntity {

    /**
     * PC id
     */
    private String pcId;

    /**
     * Option Code
     */
    private String optionCode;

    /**
     * Feature Code
     */
    private String featureCode;

    /**
     * 勾选状态，取值Select、Unselect
     */
    private String selectStatus;

    /**
     * 勾选是否可编辑，取值Yes、No
     */
    private String selectCanEdit;

    /**
     * 类型，0-正常，1-From Base Vehicle，2-From PC
     */
    private Integer type;

    @Override
    public String toString() {
        return "BomsProductConfigOption{" +
                "id = " + getId() +
                ", pcId = " + pcId +
                ", optionCode = " + optionCode +
                ", featureCode = " + featureCode +
                ", selectStatus = " + selectStatus +
                ", selectCanEdit = " + selectCanEdit +
                ", type = " + type +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
