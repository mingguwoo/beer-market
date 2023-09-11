package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.event.OxoVersionSnapshotPublishEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodelyear.ProductConfigModelYearRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/9/6
 */
@Component
@RequiredArgsConstructor
public class ModelYearOxoReleaseHandler implements EventHandler<OxoVersionSnapshotPublishEvent> {

    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final ProductConfigModelYearRepository productConfigModelYearRepository;

    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull OxoVersionSnapshotPublishEvent event) {
        OxoListRespDto oxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot(event.getOxoVersionSnapshotAggr().getOxoSnapshot());
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
        productConfigModelYearRepository.batchSave(saveModelYearAggrList);
    }

}
