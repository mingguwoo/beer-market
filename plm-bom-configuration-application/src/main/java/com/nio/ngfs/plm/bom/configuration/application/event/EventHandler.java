package com.nio.ngfs.plm.bom.configuration.application.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;

/**
 * 事件处理器
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public interface EventHandler<E extends DomainEvent> {

    /**
     * 处理事件
     *
     * @param event 事件
     */
    void handle(E event);

}
