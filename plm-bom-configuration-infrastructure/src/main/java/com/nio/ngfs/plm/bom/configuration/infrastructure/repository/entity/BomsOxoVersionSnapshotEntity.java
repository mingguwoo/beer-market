package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wangchao.wang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_version_snapshot")
public class BomsOxoVersionSnapshotEntity extends BaseEntity{

    private String modelCode;

    private String version;

    private String oxoSnapshot;

    private String type;

    private String brand;

    private String title;

    private String changeContent;

    private String emailGroup;
}
