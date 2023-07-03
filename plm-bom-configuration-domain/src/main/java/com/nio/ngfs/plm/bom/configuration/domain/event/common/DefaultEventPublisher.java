package com.nio.ngfs.plm.bom.configuration.domain.event.common;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Component
@RequiredArgsConstructor
public class DefaultEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
