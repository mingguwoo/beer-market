package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 配置管理-Rule Option表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-10-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_configuration_rule_option")
public class BomsConfigurationRuleOptionEntity extends BaseEntity {

    /**
     * Rule Id
     */
    private Long ruleId;

    /**
     * Rule条件的Option Code
     */
    private String drivingOptionCode;

    /**
     * Rule条件的Feature Code
     */
    private String drivingFeatureCode;

    /**
     * Rule结果的Option Code
     */
    private String constrainedOptionCode;

    /**
     * Rule结果的Feature Code
     */
    private String constrainedFeatureCode;

    /**
     * 矩阵打点，1-Inclusive，2-Exclusive，3-Unavailable
     */
    private Integer matrixValue;

    @Override
    public String toString() {
        return "BomsConfigurationRuleOption{" +
                "id = " + getId() +
                ", ruleId = " + ruleId +
                ", drivingOptionCode = " + drivingOptionCode +
                ", drivingFeatureCode = " + drivingFeatureCode +
                ", constrainedOptionCode = " + constrainedOptionCode +
                ", constrainedFeatureCode = " + constrainedFeatureCode +
                ", matrixValue = " + matrixValue +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
