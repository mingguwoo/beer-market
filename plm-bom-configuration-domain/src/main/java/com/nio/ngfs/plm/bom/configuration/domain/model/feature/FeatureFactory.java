package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr create(AddGroupCmd cmd) {
        return FeatureAggr.builder()
                .featureCode(cmd.getGroupCode())
                .type(FeatureTypeEnum.GROUP.getType())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .createUser("")
                .updateUser("")
                .build();
    }

}
