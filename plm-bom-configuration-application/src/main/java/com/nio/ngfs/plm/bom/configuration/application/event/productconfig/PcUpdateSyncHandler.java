package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUpdatePcDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Component
@RequiredArgsConstructor
public class PcUpdateSyncHandler implements EventHandler<PcUpdateEvent> {

    private final ProductConfigFacade productConfigFacade;

    @Override
    @Async
    public void onApplicationEvent(@NotNull PcUpdateEvent event) {
        productConfigFacade.syncUpdatePcToEnovia(buildSyncUpdatePcDto(event));
    }

    private SyncUpdatePcDto buildSyncUpdatePcDto(PcUpdateEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncUpdatePcDto dto = new SyncUpdatePcDto();
        dto.setPcId(productConfigAggr.getPcId());
        dto.setName(productConfigAggr.getName());
        dto.setModel(productConfigAggr.getModelCode());
        dto.setModelYear(productConfigAggr.getModelYear());
        return dto;
    }

}
