package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * oxo Feature/Option行信息表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_feature_option")
public class BomsOxoFeatureOptionEntity extends BaseEntity {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * 配置code
     */
    private String featureCode;

    private String ruleCheck;

    /**
     * 类型，Feature、Option
     */
    private String type;

    /**
     * 评论
     */
    private String comment;

    /**
     * 排序id
     */
    private Integer sort;

    /**
     * 软删除 1删除 0未删除
     */
    private Short softDelete;

    @Override
    public String toString() {
        return "BomsOxoFeatureOption{" +
            "id = " + getId() +
            ", modelCode = " + modelCode +
            ", featureCode = " + featureCode +
            ", ruleCheck = " + ruleCheck +
            ", type = " + type +
            ", comment = " + comment +
            ", sort = " + sort +
            ", softDelete = " + softDelete +
            ", createTime = " + getCreateTime() +
            ", updateTime = " + getUpdateTime() +
            ", createUser = " + getCreateUser() +
            ", updateUser = " + getUpdateUser() +
            ", delFlag = " + getDelFlag() +
        "}";
    }
}
