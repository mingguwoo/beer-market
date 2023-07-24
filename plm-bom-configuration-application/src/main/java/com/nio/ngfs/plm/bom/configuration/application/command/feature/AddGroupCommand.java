package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 新增Group
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class AddGroupCommand extends AbstractLockCommand<AddGroupCmd, AddGroupRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(AddGroupCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    public AddGroupRespDto executeWithLock(AddGroupCmd cmd) {
        // 使用工厂创建聚合根
        FeatureAggr featureAggr = FeatureFactory.createGroup(cmd);
        // 聚合根操作新增Group
        featureAggr.addGroup();
        // 校验GroupCode唯一
        featureDomainService.checkGroupCodeUnique(featureAggr);
        // 校验GroupCode在3DE Group Library库中是否存在
        featureDomainService.checkGroupCodeExistInGroupLibrary(featureAggr.getFeatureId().getFeatureCode(), true);
        // Repository保存聚合根
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr, FeatureChangeTypeEnum.GROUP_ADD));
        return new AddGroupRespDto();
    }

}
