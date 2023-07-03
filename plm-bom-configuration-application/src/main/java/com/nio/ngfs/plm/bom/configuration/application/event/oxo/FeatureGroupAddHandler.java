package com.nio.ngfs.plm.bom.configuration.application.event.oxo;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.event.feature.AddGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Component
public class FeatureGroupAddHandler implements EventHandler<AddGroupEvent> {

    @Override
    @EventListener
    public void handle(AddGroupEvent event) {
    }

}
