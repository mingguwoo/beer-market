package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * BOM中台V36 Code Library配置库
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-09-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_v36_code_library")
public class BomsV36CodeLibraryEntity extends BaseEntity {

    /**
     * Code
     */
    private String code;

    /**
     * Parent Code
     */
    private String parentCode;

    /**
     * Parent Code Id
     */
    private Long parentId;

    /**
     * 类型，Digit/Option
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
     * Sales Feature列表，逗号分隔
     */
    private String salesFeatureList;

    /**
     * 备注说明
     */
    private String remark;

    @Override
    public String toString() {
        return "BomsV36CodeLibrary{" +
                "id = " + getId() +
                ", code = " + code +
                ", parentCode = " + parentCode +
                ", parentId = " + parentId +
                ", type = " + type +
                ", displayName = " + displayName +
                ", chineseName = " + chineseName +
                ", salesFeatureList = " + salesFeatureList +
                ", remark = " + remark +
                ", createUser = " + getCreateUser() +
                ", updateUser = " + getUpdateUser() +
                ", createTime = " + getCreateTime() +
                ", updateTime = " + getUpdateTime() +
                ", delFlag = " + getDelFlag() +
                "}";
    }

}
