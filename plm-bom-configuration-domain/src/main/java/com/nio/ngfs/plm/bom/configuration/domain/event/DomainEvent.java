package com.nio.ngfs.plm.bom.configuration.domain.event;

import org.springframework.context.ApplicationEvent;

/**
 * 领域事件
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public abstract class DomainEvent extends ApplicationEvent {

    public DomainEvent() {
        super("");
    }

}
