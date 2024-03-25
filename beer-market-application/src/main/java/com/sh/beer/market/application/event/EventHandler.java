package com.sh.beer.market.application.event;


import com.sh.beer.market.domain.event.DomainEvent;
import org.springframework.context.ApplicationListener;

/**
 * 事件处理器
 *
 * @author
 * @date 2023/7/3
 */
public interface EventHandler<E extends DomainEvent> extends ApplicationListener<E> {
}
