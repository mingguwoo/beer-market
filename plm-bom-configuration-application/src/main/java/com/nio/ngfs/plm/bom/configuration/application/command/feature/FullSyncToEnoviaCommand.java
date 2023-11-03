package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.feature.common.AbstractFeatureCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.FullSyncToEnoviaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FullSyncToEnoviaRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 全量同步到3DE
 *
 * @author xiaozhou.tu
 * @date 2023/10/30
 */
@Component
@RequiredArgsConstructor
public class FullSyncToEnoviaCommand extends AbstractFeatureCommand<FullSyncToEnoviaCmd, FullSyncToEnoviaRespDto> {

    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(FullSyncToEnoviaCmd cmd) {
        return RedisKeyConstant.FEATURE_FULL_SYNC_TO_ENOVIA;
    }

    @Override
    protected Long getLockTime(FullSyncToEnoviaCmd cmd) {
        return 60L;
    }

    @Override
    protected FullSyncToEnoviaRespDto executeWithLock(FullSyncToEnoviaCmd cmd) {
        List<FeatureAggr> groupList = featureRepository.getGroupList();
        groupList.forEach(groupAggr -> {
            List<FeatureAggr> featureAggrList = featureRepository.queryByParentFeatureCodeAndType(groupAggr.getFeatureCode(), FeatureTypeEnum.FEATURE.getType());
            // 先同步Feature
            featureAggrList.forEach(featureAggr -> {
                eventPublisher.publish(new FeatureChangeEvent(featureAggr));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
            // 再同步Option
            featureAggrList.forEach(featureAggr -> {
                List<FeatureAggr> optionAggrList = featureRepository.queryByParentFeatureCodeAndType(featureAggr.getFeatureCode(), FeatureTypeEnum.OPTION.getType());
                optionAggrList.forEach(optionAggr -> {
                    optionAggr.setParent(featureAggr);
                    eventPublisher.publish(new FeatureChangeEvent(optionAggr));
                });
            });
        });
        return new FullSyncToEnoviaRespDto();
    }

}
