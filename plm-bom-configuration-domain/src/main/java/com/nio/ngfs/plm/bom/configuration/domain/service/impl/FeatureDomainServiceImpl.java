package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.event.feature.AddGroupEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.AddGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.EditGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Service
@RequiredArgsConstructor
public class FeatureDomainServiceImpl implements FeatureDomainService {

    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroup(AddGroupDO addGroupDO) {
        // 1、新增操作，使用工厂创建聚合根
        // 2、调用聚合根自己可以完成的操作
        // 3、领域服务完成的操作
        // 4、Repository保存
        FeatureAggr featureAggr = FeatureFactory.create(addGroupDO);
        featureAggr.addGroup();
        checkGroupCodeUnique(featureAggr);
        featureRepository.save(featureAggr);
        eventPublisher.publish(new AddGroupEvent());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editGroup(EditGroupDO editGroupDO) {
        // 1、更新/删除操作，Repository查询聚合根
        // 2、调用聚合根自己可以完成的操作
        // 3、领域服务完成的操作
        // 4、Repository保存
        FeatureAggr featureAggr = checkAndGetFeatureAggr(editGroupDO.getId(), true);
        checkGroupCodeUnique(featureAggr);
        featureAggr.editGroup(editGroupDO);
        if (featureAggr.isStatusChanged() && featureAggr.isActive()) {
            // status由Inactive更新为Active，更新Option的status
        }
        featureRepository.save(featureAggr);
    }

    private FeatureAggr checkAndGetFeatureAggr(Long id, boolean containsChildren) {
        FeatureAggr featureAggr = featureRepository.find(id);
        if (featureAggr == null) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
        if (containsChildren) {
            featureAggr.setChildrenList(featureRepository.queryByParentFeatureCode(featureAggr.getFeatureCode()));
        }
        return featureAggr;
    }

    private void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr originFeatureAggr = featureRepository.find(featureAggr.getFeatureCode(), FeatureTypeEnum.GROUP);
        if (originFeatureAggr != null) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
    }

}
