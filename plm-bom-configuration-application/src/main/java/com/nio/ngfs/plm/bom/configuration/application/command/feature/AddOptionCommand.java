package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 新增Option
 *
 * @author bill.wang
 * @date 2023/7/12
 */
@Component
@RequiredArgsConstructor
public class AddOptionCommand extends AbstractLockCommand<AddOptionCmd, AddOptionRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(AddOptionCmd cmd) {
        return RedisKeyConstant.FEATURE_FEATURE_LOCK_KEY_PREFIX + cmd.getFeatureCode();
    }

    @Override
    protected AddOptionRespDto executeWithLock(AddOptionCmd cmd) {
        FeatureAggr parentFeatureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getFeatureCode(), FeatureTypeEnum.FEATURE);
        FeatureAggr featureAggr = FeatureFactory.createOption(cmd, parentFeatureAggr);
        featureAggr.addOption();
        featureDomainService.checkFeatureOptionCodeUnique(featureAggr);
        featureDomainService.checkDisplayNameUnique(featureAggr);
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr));
        return new AddOptionRespDto(featureAggr.getId());
    }

}
