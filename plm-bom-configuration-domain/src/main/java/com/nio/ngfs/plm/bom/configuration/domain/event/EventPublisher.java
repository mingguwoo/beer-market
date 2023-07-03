package com.nio.ngfs.plm.bom.configuration.domain.event;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
public interface EventPublisher {

    /**
     * 发布事件
     *
     * @param event 领域事件
     */
    void publish(DomainEvent event);

}
