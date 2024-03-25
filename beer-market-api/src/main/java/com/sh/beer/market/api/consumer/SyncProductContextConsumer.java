package com.sh.beer.market.api.consumer;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/9/14
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextConsumer {

    /*private final SyncProductContextCommand syncProductContextCommand;

    @KafkaListener(topics = "${kafka.topic.syncProductContext}", containerFactory = "syncProductContextKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        SyncProductContextKafkaCmd cmd = GsonUtils.fromJson((String) consumerRecord.value(), SyncProductContextKafkaCmd.class);
        syncProductContextCommand.execute(cmd);
    }*/

}
