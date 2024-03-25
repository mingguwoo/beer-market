package com.sh.beer.market.application.event.productconfig;


import com.sh.beer.market.application.event.EventHandler;
import com.sh.beer.market.domain.model.productcontext.event.SyncProductContextEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author
 * @date 2023/9/6
 */
@Component
@RequiredArgsConstructor
public class ModelYearOxoReleaseHandler implements EventHandler<SyncProductContextEvent> {

    /*private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final ProductConfigModelYearRepository productConfigModelYearRepository;*/

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull SyncProductContextEvent event) {
        /*OxoListRespDto oxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot(event.getOxoVersionSnapshotAggr().getOxoSnapshot());
        if (oxoListRespDto == null || CollectionUtils.isEmpty(oxoListRespDto.getOxoHeadResps())) {
            return;
        }
        List<ProductConfigModelYearAggr> productConfigModelYearAggrList = oxoListRespDto.getOxoHeadResps().stream().map(head ->
                ProductConfigModelYearFactory.create(head.getModelCode(), head.getModelYear(), event.getOxoVersionSnapshotAggr().getCreateUser())
        ).toList();
        List<ProductConfigModelYearAggr> existModelYearAggrList = productConfigModelYearRepository.queryByModel(event.getOxoVersionSnapshotAggr().getModelCode());
        Map<String, ProductConfigModelYearAggr> existModelYearAggrMap = LambdaUtil.toKeyMap(existModelYearAggrList, ProductConfigModelYearAggr::getModelYear);
        List<ProductConfigModelYearAggr> saveModelYearAggrList = productConfigModelYearAggrList.stream().map(aggr -> {
            ProductConfigModelYearAggr saveAggr = aggr;
            ProductConfigModelYearAggr existAggr = existModelYearAggrMap.get(aggr.getModelYear());
            if (existAggr != null) {
                saveAggr = existAggr;
            }
            return saveAggr;
        }).filter(ProductConfigModelYearAggr::oxoReleased).toList();
        productConfigModelYearRepository.batchSave(saveModelYearAggrList);*/
    }

}
