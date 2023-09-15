package com.nio.ngfs.plm.bom.configuration.infrastructure.kafka;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.config.kafka.KafkaTopicProperties;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigOptionKafkaCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class KafkaSender {

    private final KafkaTopicProperties kafkaTopicProperties;
    @Resource(name = "commonKafkaTemplate")
    private KafkaTemplate<String, String> commonKafkaTemplate;

    public void sendSyncProductConfig(SyncProductConfigOptionKafkaCmd kafkaCmd) {
        commonKafkaTemplate.send(kafkaTopicProperties.getSyncProductConfig(), String.format("%s::%s", kafkaCmd.getPcId(), kafkaCmd.getOptionCode())
                , GsonUtils.toJson(kafkaCmd));
    }

}
