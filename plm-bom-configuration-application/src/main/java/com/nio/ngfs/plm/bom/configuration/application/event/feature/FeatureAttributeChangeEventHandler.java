package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Feature属性变更事件Handler
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class FeatureAttributeChangeEventHandler implements EventHandler<FeatureAttributeChangeEvent> {

    private final FeatureDomainService featureDomainService;

    @Override
    @EventListener
    public void handle(FeatureAttributeChangeEvent event) {
        if (event.getBeforeFeatureAggr() == null || event.getAfterFeatureAggr() == null) {
            return;
        }
        List<FeatureChangeLogDo> featureChangeLogDoList = Lists.newArrayList();
        collectFeatureChangeLog(featureChangeLogDoList, event.getBeforeFeatureAggr(), event.getAfterFeatureAggr());
        featureDomainService.saveFeatureChangeLog(featureChangeLogDoList);
    }

    private void collectFeatureChangeLog(List<FeatureChangeLogDo> featureChangeLogDoList, FeatureAggr before, FeatureAggr after) {
        // 更新人
        String updateUser = after.getUpdateUser();
        handleFeatureChange(featureChangeLogDoList, updateUser, before, after);
        if (CollectionUtils.isNotEmpty(before.getChildrenList())) {
            Map<Long, FeatureAggr> afterChildrenMap = LambdaUtil.toKeyMap(after.getChildrenList(), FeatureAggr::getId);
            before.getChildrenList().forEach(beforeChildren -> {
                FeatureAggr afterChildren = afterChildrenMap.get(beforeChildren.getId());
                handleFeatureChange(featureChangeLogDoList, updateUser, beforeChildren, afterChildren);
            });
        }
    }

    private void handleFeatureChange(List<FeatureChangeLogDo> featureChangeLogDoList, String updateUser, FeatureAggr before, FeatureAggr after) {
        if (before == null || after == null) {
            return;
        }
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, i -> i.getFeatureId().getFeatureCode(), ConfigConstants.FEATURE_ATTRIBUTE_FEATURE_CODE);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getParentFeatureCode, ConfigConstants.FEATURE_ATTRIBUTE_GROUP);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getDisplayName, ConfigConstants.FEATURE_ATTRIBUTE_DISPLAY_NAME);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getChineseName, ConfigConstants.FEATURE_ATTRIBUTE_CHINESE);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getDescription, ConfigConstants.FEATURE_ATTRIBUTE_DESCRIPTION);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getCatalog, ConfigConstants.FEATURE_ATTRIBUTE_CATALOG);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getRequestor, ConfigConstants.FEATURE_ATTRIBUTE_REQUESTOR);
        handleAttributeChange(featureChangeLogDoList, updateUser, before, after, FeatureAggr::getStatus, ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
    }

    private void handleAttributeChange(List<FeatureChangeLogDo> featureChangeLogDoList, String updateUser, FeatureAggr before, FeatureAggr after,
                                       Function<FeatureAggr, String> valueFunction, String attributeName) {
        String oldValue = valueFunction.apply(before);
        String newValue = valueFunction.apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return;
        }
        FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
        featureChangeLogDo.setFeatureId(before.getId());
        featureChangeLogDo.setChangeAttribute(attributeName);
        featureChangeLogDo.setOldValue(oldValue);
        featureChangeLogDo.setNewValue(newValue);
        featureChangeLogDo.setCreateUser(updateUser);
        featureChangeLogDo.setUpdateUser(updateUser);
        featureChangeLogDoList.add(featureChangeLogDo);
    }

}
