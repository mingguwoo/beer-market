package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Feature状态变更事件处理
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class FeatureStatusChangeEventHandler implements EventHandler<FeatureStatusChangeEvent> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    @Async("asyncEventExecutor")
    public void onApplicationEvent(@NotNull FeatureStatusChangeEvent event) {
        List<FeatureChangeLogDo> featureChangeLogDoList = featureDomainService.buildStatusChangeLogByGroupFeatureAndOption(event);
        featureRepository.batchSaveFeatureChangeLog(featureChangeLogDoList);
    }

}
