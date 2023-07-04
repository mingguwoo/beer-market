package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupRequest;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr create(AddGroupRequest addGroupDO) {
        return FeatureAggr.builder()
                .featureCode(addGroupDO.getGroupCode())
                .displayName(addGroupDO.getDisplayName())
                .chineseName(addGroupDO.getChineseName())
                .description(addGroupDO.getDescription())
                .createUser("")
                .updateUser("")
                .build();
    }

}
