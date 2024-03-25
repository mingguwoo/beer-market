package com.sh.beer.market.domain.event;


import org.springframework.context.ApplicationEvent;

/**
 * 领域事件
 *
 * @author
 * @date 2023/7/3
 */
public abstract class DomainEvent extends ApplicationEvent {

    public DomainEvent() {
        super("");
    }

}
