package com.nio.ngfs.plm.bom.configuration.infrastructure.config.kafka;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final CommonKafkaProperties commonKafkaProperties;

    @Bean(name = "syncProductContextKafkaContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> syncProductContextKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs(commonKafkaProperties.getSyncProductContextUsername(),
                commonKafkaProperties.getSyncProductContextPassword(), commonKafkaProperties.getSyncProductContextGroupId())));
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }

    @Bean(name = "syncProductConfigKafkaContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> syncProductConfigKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs(commonKafkaProperties.getSyncProductConfigUsername(),
                commonKafkaProperties.getSyncProductConfigPassword(), commonKafkaProperties.getSyncProductConfigGroupId())));
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }

    /**
     * 消费具体配置
     */
    public Map<String, Object> consumerConfigs(String userName, String password, String groupId) {
        Map<String, Object> propsMap = Maps.newHashMap();
        //kafka服务后台
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, commonKafkaProperties.getServers());
        //自动提交间隔时间
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, commonKafkaProperties.getAutoCommitInterval());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //消费组id
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, commonKafkaProperties.getAutoOffsetReset());
        //是否自动提交
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000);
        //发送大小配置
        propsMap.put(ConsumerConfig.SEND_BUFFER_CONFIG, 136314880);
        //最大拉去配置
        propsMap.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 131072000);
        //收取大小配置
        propsMap.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 136314880);
        propsMap.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username="
                + userName + " password=" + password + ";");
        propsMap.put("security.protocol", "SASL_PLAINTEXT");
        propsMap.put("sasl.mechanism", "PLAIN");
        return propsMap;
    }

}
