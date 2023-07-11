package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.Command;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
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
public class ChangeGroupStatusCommand implements Command<ChangeGroupStatusCmd, ChangeGroupStatusRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    public ChangeGroupStatusRespDto execute(ChangeGroupStatusCmd cmd) {
        // 查询聚合根
        FeatureAggr featureAggr = featureRepository.getById(cmd.getGroupId());
        if (featureAggr == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS);
        }
        FeatureStatusChangeTypeEnum changeTypeEnum = featureAggr.changeGroupStatus(cmd.getStatus());
        featureDomainService.changeGroupFeatureOptionStatusByGroup(featureAggr, changeTypeEnum);
        return new ChangeGroupStatusRespDto();
    }

}
