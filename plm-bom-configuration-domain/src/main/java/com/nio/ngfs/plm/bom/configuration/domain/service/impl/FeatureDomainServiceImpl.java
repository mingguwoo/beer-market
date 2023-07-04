package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.bom.share.enums.ErrorCodeEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
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
        FeatureAggr featureAggr = featureRepository.find(id);
        if (featureAggr == null) {
            throw new BusinessException(ErrorCodeEnum.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
        if (containsChildren) {
            featureAggr.setChildrenList(featureRepository.queryByParentFeatureCode(featureAggr.getFeatureCode()));
        }
        return featureAggr;
    }

    @Override
    public void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr originFeatureAggr = featureRepository.find(featureAggr.getFeatureCode(), FeatureTypeEnum.GROUP);
        if (originFeatureAggr != null) {
            throw new BusinessException(ErrorCodeEnum.FEATURE_ADD_GROUP_GROUP_CODE_REPEAT);
        }
    }

}
