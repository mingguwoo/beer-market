package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum) {
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.NO_CHANGE) {
            return;
        }
        if (!featureAggr.isType(FeatureTypeEnum.GROUP)) {
            return;
        }
        // 状态由Active变为Inactive
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.ACTIVE_TO_INACTIVE) {
            featureRepository.batchUpdateStatus(Lists.newArrayList(featureAggr.getId()), FeatureStatusEnum.INACTIVE.getStatus());
            return;
        }
        // 状态由Inactive变为Active
        List<String> featureCodeList = LambdaUtil.map(featureAggr.getChildrenList(), i -> i.getFeatureId().getFeatureCode());
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        List<Long> idList = LambdaUtil.map(optionList, FeatureAggr::getId);
        idList.addAll(LambdaUtil.map(featureAggr.getChildrenList(), FeatureAggr::getId));
        idList.add(featureAggr.getId());
        // 批量更新Group/Feature/Option的状态为Active
        featureRepository.batchUpdateStatus(idList, FeatureStatusEnum.ACTIVE.getStatus());
    }

}
