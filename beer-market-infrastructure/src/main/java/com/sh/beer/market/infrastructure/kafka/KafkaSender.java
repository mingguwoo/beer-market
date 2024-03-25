package com.sh.beer.market.infrastructure.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class KafkaSender {

    /*private final KafkaTopicProperties kafkaTopicProperties;
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
    }*/

}
