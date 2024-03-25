package com.sh.beer.market.application.event.feature;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Feature属性变更记录处理
 *
 * @author
 * @date 2023/7/25
 */
@Component
@RequiredArgsConstructor
public class FeatureChangeLogHandler {

    /*private final FeatureChangeLogDomainService featureChangeLogDomainService;
    private final FeatureChangeLogRepository featureChangeLogRepository;
    private final FeatureRepository featureRepository;

    @EventListener
    @Async("eventExecutor")
    public void onFeatureAttributeChangeEvent(@NotNull FeatureAttributeChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = featureChangeLogDomainService.buildFeatureAttributeChangeLog(event);
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

    @EventListener
    @Async("eventExecutor")
    public void onFeatureStatusChangeEvent(@NotNull FeatureStatusChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = featureChangeLogDomainService.buildStatusChangeLogByGroupFeatureAndOption(event);
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }

    @EventListener
    @Async("eventExecutor")
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
    @Async("eventExecutor")
    public void onFeatureGroupCodeChangeEvent(@NotNull FeatureGroupCodeChangeEvent event) {
        List<FeatureChangeLogAggr> changeLogAggrList = LambdaUtil.map(event.getFeature().getChildrenList(), option ->
                FeatureChangeLogFactory.create(option.getId(), event)
        );
        featureChangeLogRepository.batchSave(changeLogAggrList);
    }*/

}
