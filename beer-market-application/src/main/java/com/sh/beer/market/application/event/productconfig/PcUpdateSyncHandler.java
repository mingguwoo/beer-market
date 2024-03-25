package com.sh.beer.market.application.event.productconfig;


import com.sh.beer.market.application.event.EventHandler;
import com.sh.beer.market.domain.model.productcontext.event.SyncProductContextEvent;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author
 * @date 2023/8/18
 */
@Component
@RequiredArgsConstructor
public class PcUpdateSyncHandler implements EventHandler<SyncProductContextEvent> {

    /*private final ProductConfigFacade productConfigFacade;*/

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull SyncProductContextEvent event) {
//        productConfigFacade.syncUpdatePcToEnovia(buildSyncUpdatePcDto(event));
    }

    /*private SyncUpdatePcDto buildSyncUpdatePcDto(PcUpdateEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncUpdatePcDto dto = new SyncUpdatePcDto();
        dto.setPcId(productConfigAggr.getPcId());
        dto.setName(productConfigAggr.getName());
        dto.setModel(productConfigAggr.getModelCode());
        dto.setModelYear(productConfigAggr.getModelYear());
        return dto;
    }*/

}
