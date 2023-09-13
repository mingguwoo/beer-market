package com.nio.ngfs.plm.bom.configuration.application.event.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextEventHandler implements EventHandler<SyncProductContextEvent> {

    private final ProductContextFacade productContextFacade;
    @Override
    @Async
    public void onApplicationEvent(SyncProductContextEvent event) {
        productContextFacade.syncAddProductContextToEnovia(event);
    }
}
