package com.nio.ngfs.plm.bom.configuration.api.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Component
public class SyncProductContextConsumer {

    @KafkaListener(topics = "${kafka.topic.syncProductContext}", containerFactory = "syncProductContextKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {

    }

}
