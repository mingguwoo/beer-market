package com.nio.ngfs.plm.bom.configuration.api.consumer;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.application.command.productcontext.SyncProductContextCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextKafkaCmd;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/14
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextConsumer {

    private final SyncProductContextCommand syncProductContextCommand;

    @KafkaListener(topics = "${kafka.topic.syncProductContext}", containerFactory = "syncProductContextKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        SyncProductContextKafkaCmd cmd = GsonUtils.fromJson((String) consumerRecord.value(), SyncProductContextKafkaCmd.class);
        syncProductContextCommand.execute(cmd);
    }

}
