package com.nio.ngfs.plm.bom.configuration.infrastructure.kafka;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.config.kafka.KafkaTopicProperties;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextKafkaCmd;
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

    public void sendSyncProductConfig(SyncProductConfigKafkaCmd kafkaCmd) {
        commonKafkaTemplate.send(kafkaTopicProperties.getSyncProductConfig(), kafkaCmd.getPcId(), GsonUtils.toJson(kafkaCmd));
    }

    public void sendSyncProductContextFeature(SyncProductContextKafkaCmd kafkaCmd) {
        commonKafkaTemplate.send(kafkaTopicProperties.getSyncProductContext(), String.format("%s", kafkaCmd.getSyncProductContextModelFeatureKafkaCmd().getModelCodeList().get(0))
                , GsonUtils.toJson(kafkaCmd));
    }

    public void sendSyncProductContextFeatureOption(SyncProductContextKafkaCmd kafkaCmd) {
        commonKafkaTemplate.send(kafkaTopicProperties.getSyncProductContext(), String.format("%s", kafkaCmd.getSyncProductContextModelFeatureOptionKafkaCmd().getModel())
                , GsonUtils.toJson(kafkaCmd));
    }

}
