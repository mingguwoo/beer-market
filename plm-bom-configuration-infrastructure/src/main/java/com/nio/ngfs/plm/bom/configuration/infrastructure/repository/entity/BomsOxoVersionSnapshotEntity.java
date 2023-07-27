package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * oxo快照表
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_version_snapshot")
public class BomsOxoVersionSnapshotEntity extends BaseEntity {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * 版本号
     */
    private String version;

    /**
     * oxo快照信息
     */
    private String oxoSnapshot;

    /**
     * Formal/Informal
     */
    private String type;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 发版标题
     */
    private String title;

    /**
     * 发布内容
     */
    private String changeContent;

    /**
     * 邮件组
     */
    private String emailGroup;

    @Override
    public String toString() {
        return "BomsOxoVersionSnapshot{" +
            "id = " + getId() +
            ", modelCode = " + modelCode +
            ", version = " + version +
            ", oxoSnapshot = " + oxoSnapshot +
            ", type = " + type +
            ", brand = " + brand +
            ", title = " + title +
            ", changeContent = " + changeContent +
            ", emailGroup = " + emailGroup +
            ", createTime = " + getCreateTime() +
            ", updateTime = " + getUpdateTime() +
            ", createUser = " + getCreateUser() +
            ", updateUser = " + getUpdateUser() +
            ", delFlag = " + getDelFlag() +
        "}";
    }
}
