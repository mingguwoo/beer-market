package com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
public class FeatureChangeLogFactory {

    public static FeatureChangeLogAggr create(Long featureId, GroupCodeChangeEvent event) {
        FeatureChangeLogAggr changeLogAggr = new FeatureChangeLogAggr();
        changeLogAggr.setFeatureId(featureId);
        changeLogAggr.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_GROUP);
        changeLogAggr.setOldValue(event.getOldGroupCode());
        changeLogAggr.setNewValue(event.getGroup().getFeatureId().getFeatureCode());
        changeLogAggr.setType(FeatureChangeLogTypeEnum.AUTO.name());
        changeLogAggr.setCreateUser(event.getGroup().getUpdateUser());
        changeLogAggr.setUpdateUser(event.getGroup().getUpdateUser());
        return changeLogAggr;
    }

    public static FeatureChangeLogAggr create(Long featureId, FeatureStatusChangeEvent event) {
        FeatureChangeLogAggr changeLogAggr = new FeatureChangeLogAggr();
        changeLogAggr.setFeatureId(featureId);
        changeLogAggr.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
        changeLogAggr.setOldValue(event.getOldStatus().getStatus());
        changeLogAggr.setNewValue(event.getNewStatus().getStatus());
        changeLogAggr.setType(FeatureChangeLogTypeEnum.AUTO.name());
        changeLogAggr.setCreateUser(event.getUpdateUser());
        changeLogAggr.setUpdateUser(event.getUpdateUser());
        return changeLogAggr;
    }

    public static FeatureChangeLogAggr create(Pair<String, Function<FeatureAggr, String>> attributeValueFunction,
                                              FeatureAggr before, FeatureAggr after, String type, String updateUser) {
        String oldValue = attributeValueFunction.getRight().apply(before);
        String newValue = attributeValueFunction.getRight().apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return null;
        }
        FeatureChangeLogAggr changeLogAggr = new FeatureChangeLogAggr();
        changeLogAggr.setFeatureId(before.getId());
        changeLogAggr.setChangeAttribute(attributeValueFunction.getLeft());
        changeLogAggr.setOldValue(oldValue);
        changeLogAggr.setNewValue(newValue);
        changeLogAggr.setType(type);
        changeLogAggr.setCreateUser(updateUser);
        changeLogAggr.setUpdateUser(updateUser);
        return changeLogAggr;
    }

}
