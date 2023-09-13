package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcDeleteEvent;
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
public class PcDeleteSyncHandler implements EventHandler<PcDeleteEvent> {

    private final ProductConfigFacade productConfigFacade;

    @Override
//    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull PcDeleteEvent event) {
        productConfigFacade.syncDeletePcToEnovia(buildSyncDeletePcDto(event));
    }

    private SyncDeletePcDto buildSyncDeletePcDto(PcDeleteEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncDeletePcDto dto = new SyncDeletePcDto();
        dto.setPcId(productConfigAggr.getPcId());
        dto.setModel(productConfigAggr.getModelCode());
        dto.setModelYear(productConfigAggr.getModelYear());
        return dto;
    }

}
