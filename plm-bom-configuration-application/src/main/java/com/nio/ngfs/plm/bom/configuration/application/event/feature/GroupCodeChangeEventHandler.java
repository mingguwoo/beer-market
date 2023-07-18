package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeLogTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Group Code变更事件处理
 *
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Component
@RequiredArgsConstructor
public class GroupCodeChangeEventHandler implements EventHandler<GroupCodeChangeEvent> {

    private final FeatureRepository featureRepository;

    @Override
    @Async("asyncEventExecutor")
    public void onApplicationEvent(@NotNull GroupCodeChangeEvent event) {
        handleGroupChangeLogWithOption(event);
    }

    /**
     * 处理Option的Group变更记录
     */
    private void handleGroupChangeLogWithOption(GroupCodeChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getFeatureCodeList())) {
            return;
        }
        // 查询Feature下面的Option列表
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(event.getFeatureCodeList(), FeatureTypeEnum.OPTION.getType());
        List<FeatureChangeLogDo> featureChangeLogDoList = buildFeatureChangeLogDoList(event, optionList);
        featureRepository.batchSaveFeatureChangeLog(featureChangeLogDoList);
    }

    private List<FeatureChangeLogDo> buildFeatureChangeLogDoList(GroupCodeChangeEvent event, List<FeatureAggr> optionList) {
        return LambdaUtil.map(optionList, option -> {
            FeatureChangeLogDo featureChangeLogDo = new FeatureChangeLogDo();
            featureChangeLogDo.setFeatureId(option.getId());
            featureChangeLogDo.setChangeAttribute(ConfigConstants.FEATURE_ATTRIBUTE_GROUP);
            featureChangeLogDo.setOldValue(event.getOldGroupCode());
            featureChangeLogDo.setNewValue(event.getNewGroupCode());
            featureChangeLogDo.setType(FeatureChangeLogTypeEnum.AUTO.name());
            featureChangeLogDo.setCreateUser(event.getUpdateUser());
            featureChangeLogDo.setUpdateUser(event.getUpdateUser());
            return featureChangeLogDo;
        });
    }

}
