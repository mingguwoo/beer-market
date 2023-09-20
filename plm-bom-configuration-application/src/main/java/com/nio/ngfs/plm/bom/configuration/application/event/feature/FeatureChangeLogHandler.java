package com.nio.ngfs.plm.bom.configuration.application.event.feature;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureGroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog.FeatureChangeLogRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureChangeLogDomainService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Feature属性变更记录处理
 *
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Component
@RequiredArgsConstructor
public class FeatureChangeLogHandler {

    private final FeatureChangeLogDomainService featureChangeLogDomainService;
    private final FeatureChangeLogRepository featureChangeLogRepository;
    private final FeatureRepository featureRepository;

    @EventListener
    @Async("eventExecutor")
    public void onFeatureAttributeChangeEvent(@NotNull FeatureAttributeChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = featureChangeLogDomainService.buildFeatureAttributeChangeLog(event);
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

    @EventListener
    @Async
    public void onFeatureStatusChangeEvent(@NotNull FeatureStatusChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = featureChangeLogDomainService.buildStatusChangeLogByGroupFeatureAndOption(event);
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

    @EventListener
    @Async
    public void onGroupCodeChangeEvent(@NotNull GroupCodeChangeEvent event) {
        // 查询Feature下面的Option列表
        List<String> featureCodeList = LambdaUtil.map(event.getGroup().getChildrenList(), i -> i.getFeatureId().getFeatureCode());
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        List<FeatureChangeLogAggr> changeLogAggrList = LambdaUtil.map(optionList, option ->
                FeatureChangeLogFactory.create(option.getId(), event)
        );
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

    @EventListener
    @Async
    public void onFeatureGroupCodeChangeEvent(@NotNull FeatureGroupCodeChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = LambdaUtil.map(event.getFeature().getChildrenList(), option ->
                FeatureChangeLogFactory.create(option.getId(), event)
        );
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

}
