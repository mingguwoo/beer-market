package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLog;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
    @Async
    @EventListener
    public void handle(FeatureAttributeChangeEvent event) {
        if (event.getBeforeFeatureAggr() == null || event.getAfterFeatureAggr() == null) {
            return;
        }
        List<FeatureChangeLog> featureChangeLogList = Lists.newArrayList();
        collectFeatureChangeLog(featureChangeLogList, event.getBeforeFeatureAggr(), event.getAfterFeatureAggr());
        featureDomainService.saveFeatureChangeLog(featureChangeLogList);
    }

    private void collectFeatureChangeLog(List<FeatureChangeLog> featureChangeLogList, FeatureAggr before, FeatureAggr after) {
        handleFeatureChange(featureChangeLogList, before, after);
        if (CollectionUtils.isNotEmpty(before.getChildrenList())) {
            Map<Long, FeatureAggr> afterChildrenMap = LambdaUtil.toKeyMap(after.getChildrenList(), FeatureAggr::getId);
            before.getChildrenList().forEach(beforeChildren -> {
                FeatureAggr afterChildren = afterChildrenMap.get(beforeChildren.getId());
                handleFeatureChange(featureChangeLogList, beforeChildren, afterChildren);
            });
        }
    }

    private void handleFeatureChange(List<FeatureChangeLog> featureChangeLogList, FeatureAggr before, FeatureAggr after) {
        if (before == null || after == null) {
            return;
        }
        handleAttributeChange(featureChangeLogList, before, after, i -> i.getFeatureId().getFeatureCode(), ConfigConstants.FEATURE_ATTRIBUTE_FEATURE_CODE);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getParentFeatureCode, ConfigConstants.FEATURE_ATTRIBUTE_GROUP);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getDisplayName, ConfigConstants.FEATURE_ATTRIBUTE_DISPLAY_NAME);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getChineseName, ConfigConstants.FEATURE_ATTRIBUTE_CHINESE);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getDescription, ConfigConstants.FEATURE_ATTRIBUTE_DESCRIPTION);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getCatalog, ConfigConstants.FEATURE_ATTRIBUTE_CATALOG);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getRequestor, ConfigConstants.FEATURE_ATTRIBUTE_REQUESTOR);
        handleAttributeChange(featureChangeLogList, before, after, FeatureAggr::getStatus, ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
    }

    private void handleAttributeChange(List<FeatureChangeLog> featureChangeLogList, FeatureAggr before, FeatureAggr after,
                                       Function<FeatureAggr, String> valueFunction, String attributeName) {
        String oldValue = valueFunction.apply(before);
        String newValue = valueFunction.apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return;
        }
        FeatureChangeLog featureChangeLog = new FeatureChangeLog();
        featureChangeLog.setFeatureId(before.getId());
        featureChangeLog.setChangeAttribute(attributeName);
        featureChangeLog.setOldValue(oldValue);
        featureChangeLog.setNewValue(newValue);
        featureChangeLogList.add(featureChangeLog);
    }

}
