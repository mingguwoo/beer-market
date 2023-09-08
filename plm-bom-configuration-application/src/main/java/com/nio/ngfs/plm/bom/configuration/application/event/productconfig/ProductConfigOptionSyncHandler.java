package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncSelectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUnselectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ProductConfig打点同步到3DE
 *
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Component
@RequiredArgsConstructor
public class ProductConfigOptionSyncHandler implements EventHandler<ProductConfigOptionChangeEvent> {

    private final ProductConfigFacade productConfigFacade;

    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull ProductConfigOptionChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getProductConfigOptionAggrList())) {
            return;
        }
        event.getProductConfigOptionAggrList().forEach(this::syncToEnovia);
    }

    private void syncToEnovia(ProductConfigOptionAggr productConfigOptionAggr) {
        if (productConfigOptionAggr.isSelect()) {
            // 同步ProductConfig勾选到3DE
            SyncSelectPcOptionDto syncDto = new SyncSelectPcOptionDto();
            syncDto.setPcId(productConfigOptionAggr.getPcId());
            syncDto.setOptionCode(productConfigOptionAggr.getOptionCode());
            syncDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
            productConfigFacade.syncSelectPcOptionToEnovia(syncDto);
        } else {
            // 同步ProductConfig取消勾选到3DE
            SyncUnselectPcOptionDto syncDto = new SyncUnselectPcOptionDto();
            syncDto.setPcId(productConfigOptionAggr.getPcId());
            syncDto.setOptionCode(productConfigOptionAggr.getOptionCode());
            syncDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
            productConfigFacade.syncUnselectPcOptionToEnovia(syncDto);
        }
    }

}
