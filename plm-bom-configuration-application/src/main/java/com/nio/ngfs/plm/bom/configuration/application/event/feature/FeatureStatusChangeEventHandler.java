package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLog;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class FeatureStatusChangeEventHandler implements EventHandler<FeatureStatusChangeEvent> {

    private final FeatureDomainService featureDomainService;

    @Override
    @Async
    @EventListener
    public void handle(FeatureStatusChangeEvent event) {
        List<FeatureChangeLog> featureChangeLogList = Lists.newArrayList();
        collectFeatureChangeLog(featureChangeLogList, event);
        featureDomainService.saveFeatureChangeLog(featureChangeLogList);
    }

    private void collectFeatureChangeLog(List<FeatureChangeLog> featureChangeLogList, FeatureStatusChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getFeatureIdList())) {
            return;
        }
        event.getFeatureIdList().forEach(featureId -> {
            FeatureChangeLog featureChangeLog = new FeatureChangeLog();
            featureChangeLog.setFeatureId(featureId);
            featureChangeLog.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
            featureChangeLog.setOldValue(event.getOldStatus().getStatus());
            featureChangeLog.setNewValue(event.getNewStatus().getStatus());
            featureChangeLogList.add(featureChangeLog);
        });
    }

}
