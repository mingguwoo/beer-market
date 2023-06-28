package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.dos.AddGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.dos.EditGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class FeatureDomainService {

    private final FeatureRepository featureRepository;

    public void addGroup(AddGroupDO addGroupDO) {
        FeatureAggr featureAggr = FeatureFactory.create(addGroupDO);
        featureAggr.addGroup();
        checkGroupCodeUnique(featureAggr);
        featureRepository.save(featureAggr);
    }

    public void editGroup(EditGroupDO editGroupDO) {
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
