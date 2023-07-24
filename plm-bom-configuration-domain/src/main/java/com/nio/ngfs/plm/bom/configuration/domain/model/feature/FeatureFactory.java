package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddOptionCmd;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;
import java.util.function.Function;

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

    public static FeatureAggr createFeature(AddFeatureCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(convertFeatureAndOptionCode(cmd.getFeatureCode()), FeatureTypeEnum.FEATURE))
                .parentFeatureCode(cmd.getGroupCode())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .catalog(cmd.getCatalog())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

    public static FeatureAggr createOption(AddOptionCmd cmd) {
        return FeatureAggr.builder()
                .featureId(new FeatureId(convertFeatureAndOptionCode(cmd.getOptionCode()).trim(), FeatureTypeEnum.OPTION.getType()))
                .parentFeatureCode(cmd.getFeatureCode())
                .displayName(cmd.getDisplayName())
                .chineseName(cmd.getChineseName())
                .description(cmd.getDescription())
                .requestor(cmd.getRequestor())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

    private static String convertFeatureAndOptionCode(String code) {
        return code.replaceAll("\\s", "").toUpperCase();
    }

    public static FeatureChangeLogDo create(Long featureId, GroupCodeChangeEvent event) {
        FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
        featureChangeLogDo.setFeatureId(featureId);
        featureChangeLogDo.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_GROUP);
        featureChangeLogDo.setOldValue(event.getOldGroupCode());
        featureChangeLogDo.setNewValue(event.getNewGroupCode());
        featureChangeLogDo.setType(FeatureChangeLogTypeEnum.AUTO.name());
        featureChangeLogDo.setCreateUser(event.getUpdateUser());
        featureChangeLogDo.setUpdateUser(event.getUpdateUser());
        return featureChangeLogDo;
    }

    public static FeatureChangeLogDo create(Long featureId, FeatureStatusChangeEvent event) {
        FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
        featureChangeLogDo.setFeatureId(featureId);
        featureChangeLogDo.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
        featureChangeLogDo.setOldValue(event.getOldStatus().getStatus());
        featureChangeLogDo.setNewValue(event.getNewStatus().getStatus());
        featureChangeLogDo.setType(FeatureChangeLogTypeEnum.AUTO.name());
        featureChangeLogDo.setCreateUser(event.getUpdateUser());
        featureChangeLogDo.setUpdateUser(event.getUpdateUser());
        return featureChangeLogDo;
    }

    public static FeatureChangeLogDo create(Pair<String, Function<FeatureAggr, String>> attributeValueFunction,
                                            FeatureAggr before, FeatureAggr after, String type, String updateUser) {
        String oldValue = attributeValueFunction.getRight().apply(before);
        String newValue = attributeValueFunction.getRight().apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return null;
        }
        FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
        featureChangeLogDo.setFeatureId(before.getId());
        featureChangeLogDo.setChangeAttribute(attributeValueFunction.getLeft());
        featureChangeLogDo.setOldValue(oldValue);
        featureChangeLogDo.setNewValue(newValue);
        featureChangeLogDo.setType(type);
        featureChangeLogDo.setCreateUser(updateUser);
        featureChangeLogDo.setUpdateUser(updateUser);
        return featureChangeLogDo;
    }

}
