package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Component
@ConfigurationProperties(prefix = "event.pool")
@Getter
@Setter
public class EventPoolProperties {

    private Integer coreSize;
    private Integer maxCoreSize;
    private Integer keepAliveSeconds;
    private String namePrefix;
    private Integer queueCapacity;

}
