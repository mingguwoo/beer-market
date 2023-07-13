package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLog;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
    private final EventPublisher eventPublisher;

    @Override
    public FeatureAggr getAndCheckFeatureAggr(String featureCode, FeatureTypeEnum typeEnum) {
        FeatureId featureId = new FeatureId(featureCode, typeEnum);
        FeatureAggr featureAggr = featureRepository.find(featureId);
        if (featureAggr == null) {
            throw new BusinessException(typeEnum == FeatureTypeEnum.GROUP ? ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS :
                    (typeEnum == FeatureTypeEnum.FEATURE ? ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS :
                            ConfigErrorCode.FEATURE_OPTION_NOT_EXISTS));
        }
        return featureAggr;
    }

    @Override
    public void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr existedFeatureAggr = featureRepository.find(featureAggr.getUniqId());
        if (existedFeatureAggr != null) {
            if (Objects.equals(featureAggr.getId(), existedFeatureAggr.getId())) {
                // id相同，代表同一条记录，忽略
                return;
            }
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_CODE_REPEAT);
        }
    }

    @Override
    public void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum, String updateUser) {
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.NO_CHANGE) {
            return;
        }
        if (!featureAggr.isType(FeatureTypeEnum.GROUP)) {
            return;
        }
        // Group的状态由Active变为Inactive
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.ACTIVE_TO_INACTIVE) {
            featureRepository.save(featureAggr);
            return;
        }
        // Group的状态状态由Inactive变为Active
        List<String> featureCodeList = LambdaUtil.map(featureAggr.getChildrenList(), i -> i.getFeatureId().getFeatureCode());
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        List<Long> idList = LambdaUtil.map(optionList, FeatureAggr::getId);
        idList.addAll(LambdaUtil.map(featureAggr.getChildrenList(), FeatureAggr::getId));
        idList.add(featureAggr.getId());
        // 批量更新Group/Feature/Option的状态为Active
        featureRepository.batchUpdateStatus(idList, FeatureStatusEnum.ACTIVE.getStatus(), updateUser);
        eventPublisher.publish(new FeatureStatusChangeEvent(idList, FeatureStatusEnum.INACTIVE, FeatureStatusEnum.ACTIVE));
    }

    @Override
    public void checkFeatureOptionCodeUnique(FeatureAggr featureAggr) {
        List<FeatureAggr> existedFeatureAggrList = featureRepository.queryByFeatureCode(featureAggr.getFeatureId().getFeatureCode());
        if (CollectionUtils.isEmpty(existedFeatureAggrList)) {
            return;
        }
        existedFeatureAggrList.forEach(existedFeatureAggr -> {
            // Feature和Option的Code不可重复
            if (existedFeatureAggr.isType(FeatureTypeEnum.FEATURE) || existedFeatureAggr.isType(FeatureTypeEnum.OPTION)) {
                throw new BusinessException(featureAggr.isType(FeatureTypeEnum.FEATURE) ?
                        ConfigErrorCode.FEATURE_FEATURE_CODE_REPEAT : ConfigErrorCode.FEATURE_OPTION_CODE_REPEAT);
            }
        });
    }

    @Override
    public void checkDisplayNameUnique(FeatureAggr featureAggr) {
        List<FeatureAggr> existedFeatureAggrList = featureRepository.queryByDisplayNameCatalogAndType(
                featureAggr.getDisplayName(), featureAggr.getCatalog(), featureAggr.getFeatureId().getType());
        if (CollectionUtils.isNotEmpty(existedFeatureAggrList)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_DISPLAY_NAME_REPEAT);
        }
    }

    @Override
    public void changeFeatureGroupCode(FeatureAggr featureAggr, String newGroupCode) {
        newGroupCode = newGroupCode.trim();
        if (Objects.equals(featureAggr.getParentFeatureCode(), newGroupCode)) {
            // Group Code未变更
            return;
        }
        FeatureAggr groupFeatureAggr = getAndCheckFeatureAggr(newGroupCode, FeatureTypeEnum.GROUP);
        if (!groupFeatureAggr.isActive()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_IS_NOT_ACTIVE);
        }
        featureAggr.setParentFeatureCode(newGroupCode);
    }

    @Override
    public void saveFeatureChangeLog(List<FeatureChangeLog> featureChangeLogList) {
        // todo
    }

}
