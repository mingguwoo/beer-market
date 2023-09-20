package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncAddPcDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcAddEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Component
@RequiredArgsConstructor
public class PcAddSyncHandler implements EventHandler<PcAddEvent> {

    private final ProductConfigFacade productConfigFacade;

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull PcAddEvent event) {
        productConfigFacade.syncAddPcToEnovia(buildSyncAddPcDto(event));
    }

    private SyncAddPcDto buildSyncAddPcDto(PcAddEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncAddPcDto dto = new SyncAddPcDto();
        BeanUtils.copyProperties(productConfigAggr, dto);
        dto.setModel(productConfigAggr.getModelCode());
        return dto;
    }

}
