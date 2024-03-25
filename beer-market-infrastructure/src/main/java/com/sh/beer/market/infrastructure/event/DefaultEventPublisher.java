package com.sh.beer.market.infrastructure.event;


import com.sh.beer.market.domain.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/7/3
 */
@Component
@RequiredArgsConstructor
public class DefaultEventPublisher implements EventPublisher {

    /*private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
*/
}
