package com.sh.beer.market.application.event.productconfig;


import com.sh.beer.market.application.event.EventHandler;
import com.sh.beer.market.domain.model.productcontext.event.SyncProductContextEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @author
 * @date 2023/8/18
 */
@Component
@RequiredArgsConstructor
public class PcAddSyncHandler implements EventHandler<SyncProductContextEvent> {

    /*private final KafkaSender kafkaSender;*/

    /**
     * 此处需要同步处理
     */
    @Override
    public void onApplicationEvent(@NotNull SyncProductContextEvent event) {
        /*SyncProductConfigKafkaCmd cmd = new SyncProductConfigKafkaCmd();
        cmd.setPcId(event.getProductConfigAggr().getPcId());
        cmd.setSyncProductConfigKafkaDto(buildSyncProductConfigKafkaDto(event));
        kafkaSender.sendSyncProductConfig(cmd);*/
    }

    /*private SyncProductConfigKafkaDto buildSyncProductConfigKafkaDto(PcAddEvent event) {
        ProductConfigAggr productConfigAggr = event.getProductConfigAggr();
        SyncProductConfigKafkaDto kafkaDto = new SyncProductConfigKafkaDto();
        BeanUtils.copyProperties(productConfigAggr, kafkaDto);
        kafkaDto.setModel(productConfigAggr.getModelCode());
        return kafkaDto;
    }*/

}
