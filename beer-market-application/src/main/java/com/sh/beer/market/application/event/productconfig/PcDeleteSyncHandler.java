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
public class PcDeleteSyncHandler implements EventHandler<SyncProductContextEvent> {

    /*private final ProductConfigFacade productConfigFacade;*/

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull SyncProductContextEvent event) {
//        productConfigFacade.syncDeletePcToEnovia(buildSyncDeletePcDto(event));
    }

    /*private SyncDeletePcDto buildSyncDeletePcDto(PcDeleteEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncDeletePcDto dto = new SyncDeletePcDto();
        dto.setPcId(productConfigAggr.getPcId());
        dto.setModel(productConfigAggr.getModelCode());
        dto.setModelYear(productConfigAggr.getModelYear());
        return dto;
    }*/

}
