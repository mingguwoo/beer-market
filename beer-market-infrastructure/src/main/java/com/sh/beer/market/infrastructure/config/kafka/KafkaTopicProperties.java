package com.sh.beer.market.infrastructure.config.kafka;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/9/15
 */
@Component
//@ConfigurationProperties(prefix = "kafka.topic")
@Getter
@Setter
public class KafkaTopicProperties {

    /*private String syncProductConfig;

    private String syncProductContext;*/

}