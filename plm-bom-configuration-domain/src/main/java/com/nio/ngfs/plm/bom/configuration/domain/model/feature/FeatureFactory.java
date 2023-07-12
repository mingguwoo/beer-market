package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr createGroup(AddGroupCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(cmd.getGroupCode().trim(), FeatureTypeEnum.GROUP.getType()))
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .createUser(cmd.getCreateUser())
                .build();
    }

    public static FeatureAggr createFeature(AddFeatureCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(convertFeatureAndOptionCode(cmd.getFeatureCode()), FeatureTypeEnum.FEATURE.getType()))
                .parentFeatureCode(cmd.getGroupCode())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .catalog(cmd.getCatalog())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .build();
    }

    private static String convertFeatureAndOptionCode(String code) {
        return code.replaceAll("\\s", "").toUpperCase();
    }

}
