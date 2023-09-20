package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.facade.FeatureFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureOptionSyncReqDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureSyncDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.OptionSyncDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
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
public class FeatureOptionSyncHandler {

    private final EventPublisher eventPublisher;
    private final FeatureFacade featureFacade;

    @EventListener
    @Async("eventExecutor")
    public void onGroupCodeChangeEvent(@NotNull GroupCodeChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getGroup().getChildrenList())) {
            return;
        }
        for (FeatureAggr feature : event.getGroup().getChildrenList()) {
            eventPublisher.publish(new FeatureChangeEvent(feature));
        }
    }

    @EventListener
    @Async("eventExecutor")
    public void onFeatureChangeEvent(@NotNull FeatureChangeEvent event) {
        FeatureAggr featureAggr = event.getChangedFeatureAggr();
        // 只处理Feature和Option变更同步
        if (featureAggr.isType(FeatureTypeEnum.GROUP)) {
            return;
        }
        if (event.getChangeType() == FeatureChangeTypeEnum.GROUP_ADD ||
                event.getChangeType() == FeatureChangeTypeEnum.GROUP_UPDATE) {
            return;
        }
        FeatureOptionSyncReqDto featureOptionSyncReqDto = null;
        if (featureAggr.isType(FeatureTypeEnum.FEATURE)) {
            // Feature新增和修改
            featureOptionSyncReqDto = buildFeatureOptionSyncReqDtoWithFeature(featureAggr, event.getChangeType());
        } else if (featureAggr.isType(FeatureTypeEnum.OPTION)) {
            // Option新增和修改
            featureOptionSyncReqDto = buildFeatureOptionSyncReqDtoWithOption(featureAggr, event.getChangeType());
        }
        if (featureOptionSyncReqDto != null) {
            // Feature/Option变更同步到3DE
            featureFacade.syncFeatureOption(featureOptionSyncReqDto);
        }
    }

    private FeatureOptionSyncReqDto buildFeatureOptionSyncReqDtoWithFeature(FeatureAggr featureAggr, FeatureChangeTypeEnum changeType) {
        FeatureOptionSyncReqDto featureOptionSyncReqDto = new FeatureOptionSyncReqDto();
        featureOptionSyncReqDto.setUpdateUser(featureAggr.getUpdateUser());
        featureOptionSyncReqDto.setFeature(buildFeatureSyncDto(featureAggr, changeType));
        return featureOptionSyncReqDto;
    }

    private FeatureOptionSyncReqDto buildFeatureOptionSyncReqDtoWithOption(FeatureAggr featureAggr, FeatureChangeTypeEnum changeType) {
        FeatureSyncDto featureSyncDto = buildFeatureSyncDto(featureAggr.getParent(), null);
        featureSyncDto.setType(FeatureOptionSyncReqDto.FeatureSyncType.NO_CHANGE);
        featureSyncDto.setOptionList(Lists.newArrayList(buildOptionSyncDto(featureAggr, changeType)));
        FeatureOptionSyncReqDto featureOptionSyncReqDto = new FeatureOptionSyncReqDto();
        featureOptionSyncReqDto.setUpdateUser(featureAggr.getUpdateUser());
        featureOptionSyncReqDto.setFeature(featureSyncDto);
        return featureOptionSyncReqDto;
    }

    private FeatureSyncDto buildFeatureSyncDto(FeatureAggr featureAggr, FeatureChangeTypeEnum changeType) {
        FeatureSyncDto featureSyncDto = new FeatureSyncDto();
        featureSyncDto.setGroupCode(featureAggr.getParentFeatureCode());
        featureSyncDto.setFeatureCode(featureAggr.getFeatureId().getFeatureCode());
        featureSyncDto.setDisplayName(featureAggr.getDisplayName());
        featureSyncDto.setChineseName(featureAggr.getChineseName());
        featureSyncDto.setDescription(featureAggr.getDescription());
        featureSyncDto.setSelectionType(featureAggr.getSelectionType());
        featureSyncDto.setCatalog(featureAggr.getCatalog());
        if (changeType != null) {
            featureSyncDto.setType(changeType == FeatureChangeTypeEnum.FEATURE_ADD ? FeatureOptionSyncReqDto.FeatureSyncType.ADD :
                    FeatureOptionSyncReqDto.FeatureSyncType.UPDATE);
        }
        return featureSyncDto;
    }

    private OptionSyncDto buildOptionSyncDto(FeatureAggr optionAggr, FeatureChangeTypeEnum changeType) {
        OptionSyncDto optionSyncDto = new OptionSyncDto();
        optionSyncDto.setOptionCode(optionAggr.getFeatureId().getFeatureCode());
        optionSyncDto.setDisplayName(optionAggr.getDisplayName());
        optionSyncDto.setChineseName(optionAggr.getChineseName());
        optionSyncDto.setDescription(optionAggr.getDescription());
        optionSyncDto.setType(changeType == FeatureChangeTypeEnum.OPTION_ADD ? FeatureOptionSyncReqDto.FeatureSyncType.ADD :
                FeatureOptionSyncReqDto.FeatureSyncType.UPDATE);
        return optionSyncDto;
    }

}
