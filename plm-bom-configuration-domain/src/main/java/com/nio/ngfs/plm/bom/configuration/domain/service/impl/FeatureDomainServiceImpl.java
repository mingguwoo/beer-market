package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Service
@RequiredArgsConstructor
public class FeatureDomainServiceImpl implements FeatureDomainService {

    private final FeatureRepository featureRepository;

    @Override
    public FeatureAggr checkAndGetFeatureAggr(Long id, boolean containsChildren) {
        FeatureAggr featureAggr = featureRepository.getById(id);
        if (featureAggr == null) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
        if (containsChildren) {
            featureAggr.setChildrenList(featureRepository.queryByParentFeatureCode(featureAggr.getId().getFeatureCode()));
        }
        return featureAggr;
    }

    @Override
    public void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr originFeatureAggr = featureRepository.find(featureAggr.getId());
        if (originFeatureAggr != null) {
            throw new BusinessException(ErrorCode.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
    }

}
