package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddOptionCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr create(AddGroupCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(cmd.getGroupCode().trim(), FeatureTypeEnum.GROUP.getType()))
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .requestor(cmd.getBrand())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

    public static FeatureAggr createOption(AddOptionCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(cmd.getOptionCode().trim(), FeatureTypeEnum.GROUP.getType()))
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

}
