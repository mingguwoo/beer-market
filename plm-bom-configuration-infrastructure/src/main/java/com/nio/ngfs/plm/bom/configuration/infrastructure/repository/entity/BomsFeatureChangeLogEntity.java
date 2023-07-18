package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * BOM中台Feature属性变更记录表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-18
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_feature_change_log")
public class BomsFeatureChangeLogEntity extends BaseEntity {

    /**
     * Feature Id
     */
    private Long featureId;

    /**
     * 变更属性
     */
    private String changeAttribute;

    /**
     * 变更属性的旧值
     */
    private String oldValue;

    /**
     * 变更属性的新值
     */
    private String newValue;

    /**
     * 变更类型，Auto/Hand
     */
    private String type;

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getChangeAttribute() {
        return changeAttribute;
    }

    public void setChangeAttribute(String changeAttribute) {
        this.changeAttribute = changeAttribute;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BomsFeatureChangeLog{" +
                "id = " + getId() +
                ", featureId = " + featureId +
                ", changeAttribute = " + changeAttribute +
                ", oldValue = " + oldValue +
                ", newValue = " + newValue +
                ", type = " + type +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }

}
