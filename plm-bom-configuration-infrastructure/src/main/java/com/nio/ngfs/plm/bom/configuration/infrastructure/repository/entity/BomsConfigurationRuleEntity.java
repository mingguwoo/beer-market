package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * <p>
 * 配置管理-Rule表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-10-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_configuration_rule")
public class BomsConfigurationRuleEntity extends BaseEntity {

    /**
     * Rule Number
     */
    private String ruleNumber;

    /**
     * Rule的版本
     */
    private String ruleVersion;

    /**
     * Group Id
     */
    private Long groupId;

    /**
     * 创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）
     */
    private Integer purpose;

    /**
     * Rule的类型，Inclusive、Exclusive、Default、Available
     */
    private String ruleType;

    /**
     * Rule的变更类型，Add、Modify、Remove
     */
    private String changeType;

    /**
     * Rule的状态，In Work、Released
     */
    private String status;

    /**
     * Rule的制造生效时间
     */
    private Date effIn;

    /**
     * Rule的制造失效时间
     */
    private Date effOut;

    /**
     * Rule的发布时间
     */
    private Date releaseDate;

    /**
     * 双向Rule对的id
     */
    private Long rulePairId;

    @Override
    public String toString() {
        return "BomsConfigurationRule{" +
                "id = " + getId() +
                ", ruleNumber = " + ruleNumber +
                ", ruleVersion = " + ruleVersion +
                ", groupId = " + groupId +
                ", purpose = " + purpose +
                ", ruleType = " + ruleType +
                ", changeType = " + changeType +
                ", status = " + status +
                ", effIn = " + effIn +
                ", effOut = " + effOut +
                ", releaseDate = " + releaseDate +
                ", rulePairId = " + rulePairId +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
