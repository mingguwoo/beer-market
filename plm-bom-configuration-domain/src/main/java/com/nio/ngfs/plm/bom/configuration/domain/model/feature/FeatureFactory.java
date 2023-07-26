package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddOptionCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public class FeatureFactory {

    public static FeatureAggr createGroup(AddGroupCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(cmd.getGroupCode().trim(), FeatureTypeEnum.GROUP))
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

    public static FeatureAggr createFeature(AddFeatureCmd cmd, FeatureAggr parentFeatureAggr) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(convertFeatureAndOptionCode(cmd.getFeatureCode()), FeatureTypeEnum.FEATURE))
                .parentFeatureCode(parentFeatureAggr.getFeatureId().getFeatureCode())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .catalog(cmd.getCatalog())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .parent(parentFeatureAggr)
                .build();
    }

    public static FeatureAggr createOption(AddOptionCmd cmd, FeatureAggr parentFeatureAggr) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(convertFeatureAndOptionCode(cmd.getOptionCode()).trim(), FeatureTypeEnum.OPTION.getType()))
                .parentFeatureCode(parentFeatureAggr.getFeatureId().getFeatureCode())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .parent(parentFeatureAggr)
                .build();
    }

    private static String convertFeatureAndOptionCode(String code) {
        return code.replaceAll("\\s", "").toUpperCase();
    }

}
