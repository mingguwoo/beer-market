package com.nio.ngfs.plm.bom.configuration.domain.service.feature.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureChangeLogDomainService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Service
@RequiredArgsConstructor
public class FeatureChangeLogDomainServiceImpl implements FeatureChangeLogDomainService {

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

    @Override
    public List<FeatureChangeLogAggr> buildStatusChangeLogByGroupFeatureAndOption(FeatureStatusChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = LambdaUtil.map(event.getFeatureIdList(), featureId ->
                FeatureChangeLogFactory.create(featureId, event)
        );
        if (changeLogAggrList.size() > 0) {
            changeLogAggrList.get(changeLogAggrList.size() - 1).setType(FeatureChangeLogTypeEnum.HAND.name());
        }
        return changeLogAggrList;
    }

    @Override
    public List<FeatureChangeLogAggr> buildFeatureAttributeChangeLog(FeatureAttributeChangeEvent event) {
        if (event.getBeforeFeatureAggr() == null || event.getAfterFeatureAggr() == null) {
            return Collections.emptyList();
        }
        List<FeatureChangeLogAggr> changeLogAggrList = Lists.newArrayList();
        FeatureAggr before = event.getBeforeFeatureAggr();
        FeatureAggr after = event.getAfterFeatureAggr();
        String updateUser = after.getUpdateUser();
        handleFeatureAggrChange(changeLogAggrList, before, after, updateUser, FeatureChangeLogTypeEnum.HAND.name());
        if (CollectionUtils.isNotEmpty(before.getChildrenList())) {
            Map<Long, FeatureAggr> afterChildrenMap = LambdaUtil.toKeyMap(after.getChildrenList(), FeatureAggr::getId);
            before.getChildrenList().forEach(beforeChildren -> {
                FeatureAggr afterChildren = afterChildrenMap.get(beforeChildren.getId());
                handleFeatureAggrChange(changeLogAggrList, beforeChildren, afterChildren, updateUser, FeatureChangeLogTypeEnum.AUTO.name());
            });
        }
        return changeLogAggrList;
    }

    /**
     * 处理FeatureAggr属性变更
     */
    private void handleFeatureAggrChange(List<FeatureChangeLogAggr> featureChangeLogDoList, FeatureAggr before, FeatureAggr after, String updateUser, String type) {
        if (before == null || after == null) {
            return;
        }
        featureChangeLogDoList.addAll(LambdaUtil.map(ATTRIBUTE_VALUE_FUNCTION_PAIR_LIST, pair ->
                FeatureChangeLogFactory.create(pair, before, after, type, updateUser)
        ));
    }

}
