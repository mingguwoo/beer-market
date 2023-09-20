package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.feature.common.AbstractFeatureCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddFeatureRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 新增Feature
 *
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
@Component
@RequiredArgsConstructor
public class AddFeatureCommand extends AbstractFeatureCommand<AddFeatureCmd, AddFeatureRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(AddFeatureCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    protected AddFeatureRespDto executeWithLock(AddFeatureCmd cmd) {
        FeatureAggr parentFeatureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getGroupCode(), FeatureTypeEnum.GROUP);
        FeatureAggr featureAggr = FeatureFactory.createFeature(cmd, parentFeatureAggr);
        featureAggr.addFeature();
        featureDomainService.checkFeatureOptionCodeUnique(featureAggr);
        featureDomainService.checkDisplayNameUnique(featureAggr);
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr));
        return new AddFeatureRespDto(featureAggr.getId());
    }

}
