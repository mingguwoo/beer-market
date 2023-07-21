package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ChangeOptionStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.ChangeOptionStatusRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 改变Option状态
 *
 * @author bill.wang
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class ChangeOptionStatusCommand extends AbstractLockCommand<ChangeOptionStatusCmd, ChangeOptionStatusRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    protected String getLockKey(ChangeOptionStatusCmd cmd) {
        return RedisKeyConstant.FEATURE_OPTION_LOCK_KEY_PREFIX + cmd.getOptionCode();
    }

    @Override
    protected ChangeOptionStatusRespDto executeWithLock(ChangeOptionStatusCmd cmd) {
        FeatureAggr featureAggr = featureDomainService.getAndCheckFeatureAggr(cmd.getOptionCode(), FeatureTypeEnum.OPTION);
        featureAggr.changeOptionStatus(cmd.getStatus(), cmd.getUpdateUser());
        featureRepository.save(featureAggr);
        return new ChangeOptionStatusRespDto();
    }

    @Override
    protected void close() {
        FeatureAggrThreadLocal.remove();
    }

}
