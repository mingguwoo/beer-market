package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.dos.AddGroupDO;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr create(AddGroupDO addGroupDO) {
        return FeatureAggr.builder()
                .featureCode(addGroupDO.getFeatureCode())
                .displayName(addGroupDO.getDisplayName())
                .chineseName(addGroupDO.getChineseName())
                .description(addGroupDO.getDescription())
                .createUser(addGroupDO.getCreateUser())
                .updateUser(addGroupDO.getCreateUser())
                .build();
    }

}
