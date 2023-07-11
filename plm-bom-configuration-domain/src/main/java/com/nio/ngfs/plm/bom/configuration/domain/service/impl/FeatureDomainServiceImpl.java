package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Service
@RequiredArgsConstructor
public class FeatureDomainServiceImpl implements FeatureDomainService {

    private final FeatureRepository featureRepository;

    @Override
    public FeatureAggr getAndCheckFeatureAggr(FeatureId featureId, String message) {
        FeatureAggr featureAggr = featureRepository.find(featureId);
        if (featureAggr == null) {
            throw new BusinessException(ConfigErrorCode.DATA_NOT_EXISTS.getCode(), message);
        }
        return featureAggr;
    }

    @Override
    public void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr existedFeatureAggr = featureRepository.find(featureAggr.getUniqId());
        if (existedFeatureAggr != null) {
            if (Objects.equals(featureAggr.getId(), existedFeatureAggr.getId())) {
                // id相同，同一条记录
                return;
            }
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_CODE_REPEAT);
        }
    }

}
