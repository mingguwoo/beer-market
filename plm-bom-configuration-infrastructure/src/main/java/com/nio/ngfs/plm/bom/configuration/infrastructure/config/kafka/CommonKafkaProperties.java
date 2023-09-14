package com.nio.ngfs.plm.bom.configuration.infrastructure.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Component
@ConfigurationProperties(prefix = "kafka.consumer.common")
@Getter
@Setter
public class CommonKafkaProperties {

    /**
     * kafka服务地址
     */
    private String servers;

    private String autoCommitInterval;

    private String autoOffsetReset;

    private String commonUsername;

    private String commonPassword;

    private String syncProductContextUsername;

    private String syncProductContextPassword;

    private String syncProductContextGroupId;

    private String syncProductConfigUsername;

    private String syncProductConfigPassword;

    private String syncProductConfigGroupId;

}
