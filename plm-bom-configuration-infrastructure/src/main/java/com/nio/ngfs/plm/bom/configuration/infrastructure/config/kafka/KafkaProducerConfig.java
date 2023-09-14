package com.nio.ngfs.plm.bom.configuration.infrastructure.config.kafka;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final CommonKafkaProperties commonKafkaProperties;

    @Bean("commonKafkaTemplate")
    public KafkaTemplate<String, String> commonKafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs(commonKafkaProperties.getCommonUsername(), commonKafkaProperties.getCommonPassword())));
    }

    private Map<String, Object> producerConfigs(String userName, String password) {
        Map<String, Object> props = Maps.newHashMap();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, commonKafkaProperties.getServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 104857600);
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, 110100480);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 110100480);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username="
                + userName + " password=" + password + ";");
        return props;
    }

}
