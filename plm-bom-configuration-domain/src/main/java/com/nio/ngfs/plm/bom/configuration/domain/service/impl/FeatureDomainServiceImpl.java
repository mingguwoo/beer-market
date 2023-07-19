package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.facade.FeatureFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Service
@RequiredArgsConstructor
public class FeatureDomainServiceImpl implements FeatureDomainService {

    @SuppressWarnings("unchecked")
    private static final List<Pair<String, Function<FeatureAggr, String>>> ATTRIBUTE_VALUE_FUNCTION_PAIR_LIST = Lists.newArrayList(
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_FEATURE_CODE, i -> i.getFeatureId().getFeatureCode()),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_GROUP, FeatureAggr::getParentFeatureCode),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_DISPLAY_NAME, FeatureAggr::getDisplayName),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_CHINESE, FeatureAggr::getChineseName),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_DESCRIPTION, FeatureAggr::getDescription),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_CATALOG, FeatureAggr::getCatalog),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_REQUESTOR, FeatureAggr::getRequestor),
            Pair.of(ConfigConstants.FEATURE_ATTRIBUTE_STATUS, FeatureAggr::getStatus)
    );

    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;
    private final FeatureFacade featureFacade;

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
        // 查询Group下面所有的Option列表
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        // 筛选Group下状态为Inactive的Feature和Option
        List<Long> idList = LambdaUtil.map(optionList, FeatureAggr::isInactive, FeatureAggr::getId);
        idList.addAll(LambdaUtil.map(featureAggr.getChildrenList(), FeatureAggr::isInactive, FeatureAggr::getId));
        idList.add(featureAggr.getId());
        // 批量更新Group/Feature/Option的状态为Active
        featureRepository.batchUpdateStatus(idList, FeatureStatusEnum.ACTIVE.getStatus(), updateUser);
        eventPublisher.publish(new FeatureStatusChangeEvent(idList, FeatureStatusEnum.INACTIVE, FeatureStatusEnum.ACTIVE, updateUser));
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
            existedFeatureAggrList.forEach(existedFeatureAggr -> {
                if (Objects.equals(featureAggr.getId(), existedFeatureAggr.getId())) {
                    // 同一条记录。跳过
                    return;
                }
                throw new BusinessException(ConfigErrorCode.FEATURE_DISPLAY_NAME_REPEAT);
            });
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
    public List<FeatureChangeLogDo> buildGroupChangeLogByOption(GroupCodeChangeEvent event, List<FeatureAggr> optionList) {
        return LambdaUtil.map(optionList, option ->
                FeatureFactory.create(option.getId(), event)
        );
    }

    @Override
    public List<FeatureChangeLogDo> buildStatusChangeLogByGroupFeatureAndOption(FeatureStatusChangeEvent event) {
        List<FeatureChangeLogDo> featureChangeLogDoList = LambdaUtil.map(event.getFeatureIdList(), featureId ->
                FeatureFactory.create(featureId, event)
        );
        if (featureChangeLogDoList.size() > 0) {
            featureChangeLogDoList.get(featureChangeLogDoList.size() - 1).setType(FeatureChangeLogTypeEnum.HAND.name());
        }
        return featureChangeLogDoList;
    }

    @Override
    public List<FeatureChangeLogDo> buildFeatureAttributeChangeLog(FeatureAttributeChangeEvent event) {
        if (event.getBeforeFeatureAggr() == null || event.getAfterFeatureAggr() == null) {
            return Collections.emptyList();
        }
        List<FeatureChangeLogDo> featureChangeLogDoList = Lists.newArrayList();
        FeatureAggr before = event.getBeforeFeatureAggr();
        FeatureAggr after = event.getAfterFeatureAggr();
        String updateUser = after.getUpdateUser();
        handleFeatureAggrChange(featureChangeLogDoList, before, after, updateUser, FeatureChangeLogTypeEnum.HAND.name());
        if (CollectionUtils.isNotEmpty(before.getChildrenList())) {
            Map<Long, FeatureAggr> afterChildrenMap = LambdaUtil.toKeyMap(after.getChildrenList(), FeatureAggr::getId);
            before.getChildrenList().forEach(beforeChildren -> {
                FeatureAggr afterChildren = afterChildrenMap.get(beforeChildren.getId());
                handleFeatureAggrChange(featureChangeLogDoList, beforeChildren, afterChildren, updateUser, FeatureChangeLogTypeEnum.AUTO.name());
            });
        }
        return featureChangeLogDoList;
    }

    @Override
    public void checkGroupCodeExistInGroupLibrary(String groupCode, boolean isAdd) {
        if (!featureFacade.isGroupExistedInGroupLibrary(groupCode)) {
            throw new BusinessException(isAdd ? ConfigErrorCode.FEATURE_ADD_GROUP_IN_3DE_FIRST :
                    ConfigErrorCode.FEATURE_UPDATE_GROUP_IN_3DE_FIRST);
        }
    }

    /**
     * 处理FeatureAggr属性变更
     */
    private void handleFeatureAggrChange(List<FeatureChangeLogDo> featureChangeLogDoList, FeatureAggr before, FeatureAggr after, String updateUser, String type) {
        if (before == null || after == null) {
            return;
        }
        featureChangeLogDoList.addAll(LambdaUtil.map(ATTRIBUTE_VALUE_FUNCTION_PAIR_LIST, pair ->
                FeatureFactory.create(pair, before, after, type, updateUser)
        ));
    }

}
