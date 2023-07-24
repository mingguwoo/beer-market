package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditFeatureCmdRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑Feature
 *
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
@Component
@RequiredArgsConstructor
public class EditFeatureCommand extends AbstractLockCommand<EditFeatureCmd, EditFeatureCmdRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(EditFeatureCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    protected EditFeatureCmdRespDto executeWithLock(EditFeatureCmd cmd) {
        FeatureAggr featureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getFeatureCode(), FeatureTypeEnum.FEATURE);
        featureAggr.editFeature(cmd);
        featureDomainService.changeFeatureGroupCode(featureAggr, cmd.getGroupCode());
        featureDomainService.checkDisplayNameUnique(featureAggr);
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr, FeatureChangeTypeEnum.FEATURE_EDIT));
        return new EditFeatureCmdRespDto();
    }

    @Override
    protected void close() {
        FeatureAggrThreadLocal.remove();
    }

}
