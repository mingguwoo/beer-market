package com.nio.ngfs.plm.bom.configuration.api.consumer;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.application.command.productconfig.SyncProductConfigCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaCmd;
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
public class SyncProductConfigConsumer {

    private final SyncProductConfigCommand syncProductConfigCommand;

    @KafkaListener(topics = "${kafka.topic.syncProductConfig}", containerFactory = "syncProductConfigKafkaContainerFactory")
    public void consume(ConsumerRecord<?, ?> consumerRecord) {
        SyncProductConfigKafkaCmd cmd = GsonUtils.fromJson((String) consumerRecord.value(), SyncProductConfigKafkaCmd.class);
        syncProductConfigCommand.execute(cmd);
    }

}
