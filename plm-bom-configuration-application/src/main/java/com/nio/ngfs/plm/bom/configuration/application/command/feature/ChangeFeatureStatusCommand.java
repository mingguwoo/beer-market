package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ChangeFeatureStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.ChangeFeatureStatusRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 改变Feature状态
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class ChangeFeatureStatusCommand extends AbstractLockCommand<ChangeFeatureStatusCmd, ChangeFeatureStatusRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    protected String getLockKey(ChangeFeatureStatusCmd cmd) {
        return RedisKeyConstant.FEATURE_FEATURE_LOCK_KEY_PREFIX + cmd.getFeatureCode();
    }

    @Override
    protected ChangeFeatureStatusRespDto executeWithLock(ChangeFeatureStatusCmd cmd) {
        FeatureAggr featureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getFeatureCode(), FeatureTypeEnum.FEATURE);
        FeatureStatusChangeTypeEnum changeTypeEnum = featureAggr.changeFeatureStatus(cmd);
        if (changeTypeEnum != FeatureStatusChangeTypeEnum.NO_CHANGE) {
            // Status变更，保存到数据库
            featureRepository.save(featureAggr);
        }
        return new ChangeFeatureStatusRespDto();
    }

    @Override
    protected void close() {
        FeatureAggrThreadLocal.remove();
    }

}
