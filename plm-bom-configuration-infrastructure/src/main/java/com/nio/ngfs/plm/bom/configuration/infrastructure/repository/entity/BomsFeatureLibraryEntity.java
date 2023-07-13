package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * BOM中台Feature Library配置库
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-11
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_feature_library")
public class BomsFeatureLibraryEntity extends BaseEntity {

    /**
     * Feature Code
     */
    private String featureCode;

    /**
     * Parent Feature Code
     */
    private String parentFeatureCode;

    /**
     * 类型，Group/Feature/Option
     */
    private String type;

    /**
     * 英文描述
     */
    private String displayName;

    /**
     * 中文描述
     */
    private String chineseName;

    /**
     * 补充描述
     */
    private String description;

    /**
     * Feature的可选性
     */
    private String selectionType;

    /**
     * Feature的必须性
     */
    private String mayMust;

    /**
     * Feature的分类
     */
    private String catalog;

    /**
     * Feature的成熟度
     */
    private String maturity;

    /**
     * 版本，颜色件相关
     */
    private String version;

    /**
     * 创建方
     */
    private String requestor;

    /**
     * 状态，Active/Inactive
     */
    private String status;

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getParentFeatureCode() {
        return parentFeatureCode;
    }

    public void setParentFeatureCode(String parentFeatureCode) {
        this.parentFeatureCode = parentFeatureCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public String getMayMust() {
        return mayMust;
    }

    public void setMayMust(String mayMust) {
        this.mayMust = mayMust;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BomsFeatureLibrary{" +
                "id = " + getId() +
                ", featureCode = " + featureCode +
                ", parentFeatureCode = " + parentFeatureCode +
                ", type = " + type +
                ", displayName = " + displayName +
                ", chineseName = " + chineseName +
                ", description = " + description +
                ", selectionType = " + selectionType +
                ", mayMust = " + mayMust +
                ", catalog = " + catalog +
                ", maturity = " + maturity +
                ", version = " + version +
                ", requestor = " + requestor +
                ", status = " + status +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateUser() +
                ", delFlag = " + getDelFlag() +
                "}";
    }

}
