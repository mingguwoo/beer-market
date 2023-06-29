package com.nio.ngfs.plm.bom.configuration.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_feature_library")
public class BomsFeatureLibraryEntity extends BaseEntity {

    private String featureCode;

    private String type;

}
