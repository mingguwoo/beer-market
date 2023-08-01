package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 编辑Group
 *
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@Component
@RequiredArgsConstructor
public class EditGroupCommand extends AbstractLockCommand<EditGroupCmd, EditGroupRespDto> {

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(EditGroupCmd cmd) {
        return RedisKeyConstant.FEATURE_GROUP_LOCK_KEY_PREFIX + cmd.getGroupCode();
    }

    @Override
    protected EditGroupRespDto executeWithLock(EditGroupCmd cmd) {
        // 查询聚合根
        FeatureAggr featureAggr = featureRepository.getById(cmd.getGroupId());
        if (featureAggr == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS);
        }
        String oldGroupCode = featureAggr.getFeatureId().getFeatureCode();
        // 聚合根操作编辑Group
        featureAggr.editGroup(cmd);
        // 校验GroupCode唯一
        featureDomainService.checkGroupCodeUnique(featureAggr);
        // 校验GroupCode在3DE Group Library库中是否存在
        featureDomainService.checkGroupCodeExistInGroupLibrary(featureAggr.getFeatureId().getFeatureCode(), false);
        // Repository保存聚合根
        featureRepository.save(featureAggr);
        eventPublisher.publish(new FeatureChangeEvent(featureAggr));
        // 发布GroupCode变更事件
        publishGroupCodeChangeEvent(featureAggr, oldGroupCode);
        return new EditGroupRespDto();
    }

    /**
     * 发布GroupCode变更事件
     */
    private void publishGroupCodeChangeEvent(FeatureAggr featureAggr, String oldGroupCode) {
        if (Objects.equals(featureAggr.getFeatureId().getFeatureCode(), oldGroupCode)) {
            return;
        }
        GroupCodeChangeEvent groupCodeChangeEvent = new GroupCodeChangeEvent();
        groupCodeChangeEvent.setGroup(featureAggr);
        groupCodeChangeEvent.setOldGroupCode(oldGroupCode);
        eventPublisher.publish(groupCodeChangeEvent);
    }

    @Override
    protected void close() {
        FeatureAggrThreadLocal.remove();
    }

}
