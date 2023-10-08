package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcAddEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.kafka.KafkaSender;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Component
@RequiredArgsConstructor
public class PcAddSyncHandler implements EventHandler<PcAddEvent> {

    private final KafkaSender kafkaSender;

    /**
     * 此处需要同步处理
     */
    @Override
    public void onApplicationEvent(@NotNull PcAddEvent event) {
        SyncProductConfigKafkaCmd cmd = new SyncProductConfigKafkaCmd();
        cmd.setPcId(event.getProductConfigAggr().getPcId());
        cmd.setSyncProductConfigKafkaDto(buildSyncProductConfigKafkaDto(event));
        kafkaSender.sendSyncProductConfig(cmd);
    }

    private SyncProductConfigKafkaDto buildSyncProductConfigKafkaDto(PcAddEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncProductConfigKafkaDto kafkaDto = new SyncProductConfigKafkaDto();
        BeanUtils.copyProperties(productConfigAggr, kafkaDto);
        kafkaDto.setModel(productConfigAggr.getModelCode());
        return kafkaDto;
    }

}
