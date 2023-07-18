package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
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

    private final FeatureRepository featureRepository;

    @Override
    @Async("asyncEventExecutor")
    public void onApplicationEvent(@NotNull FeatureStatusChangeEvent event) {
        List<FeatureChangeLogDo> featureChangeLogDoList = Lists.newArrayList();
        collectFeatureChangeLog(featureChangeLogDoList, event);
        featureRepository.batchSaveFeatureChangeLog(featureChangeLogDoList);
    }

    private void collectFeatureChangeLog(List<FeatureChangeLogDo> featureChangeLogDoList, FeatureStatusChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getFeatureIdList())) {
            return;
        }
        event.getFeatureIdList().forEach(featureId -> {
            FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
            featureChangeLogDo.setFeatureId(featureId);
            featureChangeLogDo.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_STATUS);
            featureChangeLogDo.setOldValue(event.getOldStatus().getStatus());
            featureChangeLogDo.setNewValue(event.getNewStatus().getStatus());
            featureChangeLogDo.setType(FeatureChangeLogTypeEnum.AUTO.name());
            featureChangeLogDo.setCreateUser(event.getUpdateUser());
            featureChangeLogDo.setUpdateUser(event.getUpdateUser());
            featureChangeLogDoList.add(featureChangeLogDo);
        });
        featureChangeLogDoList.get(featureChangeLogDoList.size() - 1).setType(FeatureChangeLogTypeEnum.HAND.name());
    }

}
