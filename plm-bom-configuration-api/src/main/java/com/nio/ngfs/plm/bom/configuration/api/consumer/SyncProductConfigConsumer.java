package com.nio.ngfs.plm.bom.configuration.api.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Component
public class SyncProductConfigConsumer {

    @KafkaListener(topics = "${kafka.topic.syncProductConfig}", containerFactory = "syncProductConfigKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {

    }

}
