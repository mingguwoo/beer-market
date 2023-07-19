package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.FeatureChangeLogDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import lombok.RequiredArgsConstructor;
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

    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;

    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull GroupCodeChangeEvent event) {
        // 查询Feature下面的Option列表
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(event.getFeatureCodeList(), FeatureTypeEnum.OPTION.getType());
        List<FeatureChangeLogDo> featureChangeLogDoList = featureDomainService.buildGroupChangeLogByOption(event, optionList);
        featureRepository.batchSaveFeatureChangeLog(featureChangeLogDoList);
    }

}
