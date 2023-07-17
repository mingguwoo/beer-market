package com.nio.ngfs.plm.bom.configuration.application.event.oxo;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupAddEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Component
public class OxoGroupAddEventHandler implements EventHandler<GroupAddEvent> {

    @Override
    public void onApplicationEvent(@NotNull GroupAddEvent event) {
    }

}
