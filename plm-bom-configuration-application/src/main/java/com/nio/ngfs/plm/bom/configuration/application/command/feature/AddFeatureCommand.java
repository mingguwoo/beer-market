package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
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
public class AddFeatureCommand extends AbstractLockCommand<AddFeatureCmd, AddFeatureRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    protected String getLockKey(AddFeatureCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    protected AddFeatureRespDto executeWithLock(AddFeatureCmd cmd) {
        FeatureAggr featureAggr = FeatureFactory.createFeature(cmd);
        FeatureAggr parentFeatureAggr = featureDomainService.getAndCheckFeatureAggr(
                new FeatureId(featureAggr.getParentFeatureCode(), FeatureTypeEnum.GROUP),
                ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS);
        featureAggr.setParent(parentFeatureAggr);
        featureAggr.addFeature();
        featureDomainService.checkFeatureOptionCodeUnique(featureAggr);
        featureDomainService.checkDisplayNameUnique(featureAggr);
        featureRepository.save(featureAggr);
        return new AddFeatureRespDto();
    }

}