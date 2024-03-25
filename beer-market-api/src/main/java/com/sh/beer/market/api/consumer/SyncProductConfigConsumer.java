package com.sh.beer.market.api.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/9/14
 */
@Component
@RequiredArgsConstructor
public class SyncProductConfigConsumer {

    /*private final SyncProductConfigCommand syncProductConfigCommand;

    @KafkaListener(topics = "${kafka.topic.syncProductConfig}", containerFactory = "syncProductConfigKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        SyncProductConfigKafkaCmd cmd = GsonUtils.fromJson((String) consumerRecord.value(), SyncProductConfigKafkaCmd.class);
        syncProductConfigCommand.execute(cmd);
    }*/

}
