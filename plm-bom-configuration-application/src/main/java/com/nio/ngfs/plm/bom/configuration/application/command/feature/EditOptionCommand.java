package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.feature.common.AbstractFeatureCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑Option
 *
 * @author bill.wang
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class EditOptionCommand extends AbstractFeatureCommand<EditOptionCmd, EditOptionRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(EditOptionCmd cmd) {
        return RedisKeyConstant.FEATURE_OPTION_LOCK_KEY_PREFIX + cmd.getOptionCode();
    }

    @Override
    protected EditOptionRespDto executeWithLock(EditOptionCmd cmd) {
        FeatureAggr featureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getOptionCode(), FeatureTypeEnum.OPTION);
        FeatureAggr parentFeatureAggr = featureDomainService.getAndCheckFeatureAggr(featureAggr.getParentFeatureCode(), FeatureTypeEnum.FEATURE);
        featureAggr.editOption(cmd, parentFeatureAggr);
        featureDomainService.checkDisplayNameUnique(featureAggr);
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr));
        return new EditOptionRespDto();
    }

}
