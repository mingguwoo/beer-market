package com.sh.beer.market.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 配置管理-Rule Group表
 * </p>
 *
 * @author
 * @since 2023-10-17
 */
@Data
@SuperBuilder
@AllArgsConstructor
//@TableName("boms_configuration_rule_group")
public class BomsConfigurationRuleGroupEntity extends BaseEntity {

    /**
     * Rule的中文描述
     *//*
    private String chineseName;

    *//**
     * Rule的英文描述
     *//*
    private String displayName;

    *//**
     * 创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）
     *//*
    private Integer purpose;

    *//**
     * Rule适用范围
     *//*
    private String definedBy;

    *//**
     * Rule的补充描述
     *//*
    private String description;

    *//**
     * Group勾选的Driving Feature Code
     *//*
    private String drivingFeature;

    *//**
     * Group勾选的Constrained Feature Code列表，多个之间逗号分隔
     *//*
    private String constrainedFeature;

    @Override
    public String toString() {
        return "BomsConfigurationRuleGroup{" +
                "id = " + getId() +
                ", chineseName = " + chineseName +
                ", displayName = " + displayName +
                ", purpose = " + purpose +
                ", definedBy = " + definedBy +
                ", description = " + description +
                ", drivingFeature = " + drivingFeature +
                ", constrainedFeature = " + constrainedFeature +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }*/
}
