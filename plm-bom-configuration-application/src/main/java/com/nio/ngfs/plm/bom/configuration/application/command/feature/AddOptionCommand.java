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
    @Override
    protected String getLockKey(AddOptionCmd cmd) {
        return RedisKeyConstant.FEATURE_OPTION_LOCK_KEY_PREFIX + cmd.getParentCode();
    }

    @Override
    protected AddOptionRespDto executeWithLock(AddOptionCmd cmd) {
        FeatureAggr featureAggr = FeatureFactory.createOption(cmd);
        featureAggr.addOption();
        featureDomainService.checkFeatureOptionCodeUnique(featureAggr);
        featureDomainService.checkDisplayNameUnique(featureAggr);
        FeatureAggr parentFeatureAggr = featureDomainService.getAndCheckFeatureAggr(
                new FeatureId(featureAggr.getParentFeatureCode(), FeatureTypeEnum.FEATURE),
                ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        featureAggr.setParent(parentFeatureAggr);
        featureAggr.setCatalog(parentFeatureAggr.getCatalog());
        featureDomainService.checkOptionChineseNameUnique(parentFeatureAggr,featureAggr);
        featureRepository.save(featureAggr);
        return new AddOptionRespDto();
    }
}
