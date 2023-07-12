package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ChangeGroupStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.ChangeGroupStatusRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@Component
@RequiredArgsConstructor
public class ChangeGroupStatusCommand extends AbstractLockCommand<ChangeGroupStatusCmd, ChangeGroupStatusRespDto> {

    private final FeatureDomainService featureDomainService;

    @Override
    protected String getLockKey(ChangeGroupStatusCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    protected ChangeGroupStatusRespDto executeWithLock(ChangeGroupStatusCmd cmd) {
        FeatureId featureId = new FeatureId(cmd.getGroupCode(), FeatureTypeEnum.GROUP);
        // 查询聚合根
        FeatureAggr featureAggr = featureDomainService.getAndCheckFeatureAggr(featureId, ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS);
        FeatureStatusChangeTypeEnum changeTypeEnum = featureAggr.changeGroupStatus(cmd.getStatus());
        featureDomainService.changeGroupFeatureOptionStatusByGroup(featureAggr, changeTypeEnum);
        return new ChangeGroupStatusRespDto();
    }

}
