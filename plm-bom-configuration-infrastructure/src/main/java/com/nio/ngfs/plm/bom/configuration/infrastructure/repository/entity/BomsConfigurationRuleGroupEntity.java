package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 配置管理-Rule Group表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-10-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_configuration_rule_group")
public class BomsConfigurationRuleGroupEntity extends BaseEntity {

    /**
     * Rule的中文描述
     */
    private String chineseName;

    /**
     * Rule的英文描述
     */
    private String displayName;

    /**
     * 创建Rule的目的，1-（Sales —> Eng），2-（Sales —> Sales），3-（Sales <—> Sales），4-（Sales X Sales），5-（Base Vehicle —> Sales）
     */
    private Integer purpose;

    /**
     * Rule适用范围
     */
    private String definedBy;

    /**
     * Rule的补充描述
     */
    private String description;

    @Override
    public String toString() {
        return "BomsConfigurationRuleGroup{" +
                "id = " + getId() +
                ", chineseName = " + chineseName +
                ", displayName = " + displayName +
                ", purpose = " + purpose +
                ", definedBy = " + definedBy +
                ", description = " + description +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }
}
