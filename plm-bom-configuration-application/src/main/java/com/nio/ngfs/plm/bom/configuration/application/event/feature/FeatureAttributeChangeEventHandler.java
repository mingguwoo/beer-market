package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Feature属性变更事件Handler
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Component
@RequiredArgsConstructor
public class FeatureAttributeChangeEventHandler implements EventHandler<FeatureAttributeChangeEvent> {

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

    @Override
    @Async("asyncEventExecutor")
    public void onApplicationEvent(@NotNull FeatureAttributeChangeEvent event) {
        if (event.getBeforeFeatureAggr() == null || event.getAfterFeatureAggr() == null) {
            return;
        }
        List<FeatureChangeLogDo> featureChangeLogDoList = Lists.newArrayList();
        collectFeatureChangeLog(featureChangeLogDoList, event.getBeforeFeatureAggr(), event.getAfterFeatureAggr());
        featureRepository.batchSaveFeatureChangeLog(featureChangeLogDoList);
    }

    private void collectFeatureChangeLog(List<FeatureChangeLogDo> featureChangeLogDoList, FeatureAggr before, FeatureAggr after) {
        // 更新人
        String updateUser = after.getUpdateUser();
        handleFeatureChange(featureChangeLogDoList, updateUser, before, after, FeatureChangeLogTypeEnum.HAND.name());
        if (CollectionUtils.isNotEmpty(before.getChildrenList())) {
            Map<Long, FeatureAggr> afterChildrenMap = LambdaUtil.toKeyMap(after.getChildrenList(), FeatureAggr::getId);
            before.getChildrenList().forEach(beforeChildren -> {
                FeatureAggr afterChildren = afterChildrenMap.get(beforeChildren.getId());
                handleFeatureChange(featureChangeLogDoList, updateUser, beforeChildren, afterChildren, FeatureChangeLogTypeEnum.AUTO.name());
            });
        }
    }

    private void handleFeatureChange(List<FeatureChangeLogDo> featureChangeLogDoList, String updateUser, FeatureAggr before, FeatureAggr after, String type) {
        if (before == null || after == null) {
            return;
        }
        ATTRIBUTE_VALUE_FUNCTION_PAIR_LIST.forEach(pair ->
                handleAttributeChange(featureChangeLogDoList, updateUser, before, after, type, pair.getRight(), pair.getLeft())
        );
    }

    private void handleAttributeChange(List<FeatureChangeLogDo> featureChangeLogDoList, String updateUser, FeatureAggr before, FeatureAggr after, String type,
                                       Function<FeatureAggr, String> valueFunction, String attributeName) {
        String oldValue = valueFunction.apply(before);
        String newValue = valueFunction.apply(after);
        if (Objects.equals(oldValue, newValue)) {
            return;
        }
        FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
        featureChangeLogDo.setFeatureId(before.getId());
        featureChangeLogDo.setChangeAttribute(attributeName);
        featureChangeLogDo.setOldValue(oldValue);
        featureChangeLogDo.setNewValue(newValue);
        featureChangeLogDo.setType(type);
        featureChangeLogDo.setCreateUser(updateUser);
        featureChangeLogDo.setUpdateUser(updateUser);
        featureChangeLogDoList.add(featureChangeLogDo);
    }

}
