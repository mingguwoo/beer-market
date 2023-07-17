package com.nio.ngfs.plm.bom.configuration.application.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import org.springframework.context.ApplicationListener;

/**
 * 事件处理器
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public interface EventHandler<E extends DomainEvent> extends ApplicationListener<E> {
}
