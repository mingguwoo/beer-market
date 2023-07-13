package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditFeatureCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditFeatureCmdRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
@Component
@RequiredArgsConstructor
public class EditFeatureCommand extends AbstractLockCommand<EditFeatureCmd, EditFeatureCmdRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

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
        return new EditFeatureCmdRespDto();
    }

}
