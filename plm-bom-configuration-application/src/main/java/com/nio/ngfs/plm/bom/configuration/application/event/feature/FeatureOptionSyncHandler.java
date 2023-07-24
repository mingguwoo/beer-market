package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Feature/Option同步处理
 *
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Component
@RequiredArgsConstructor
public class FeatureOptionSyncHandler implements EventHandler<FeatureChangeEvent> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull FeatureChangeEvent event) {
        FeatureAggr featureAggr = event.getChangedFeatureAggr();
        if (featureAggr.isType(FeatureTypeEnum.GROUP)) {
            return;
        }
        if (event.getChangeType() == FeatureChangeTypeEnum.GROUP_ADD ||
                event.getChangeType() == FeatureChangeTypeEnum.GROUP_EDIT) {
            return;
        }
    }

}
