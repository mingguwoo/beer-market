package com.sh.beer.market.application.event.productconfig;


import com.sh.beer.market.application.event.EventHandler;
import com.sh.beer.market.domain.model.productcontext.event.SyncProductContextEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * ProductConfig打点同步到3DE
 *
 * @author
 * @date 2023/9/8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigOptionSyncHandler implements EventHandler<SyncProductContextEvent> {

    /*private final KafkaSender kafkaSender;*/

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull SyncProductContextEvent event) {
        /*if (CollectionUtils.isEmpty(event.getProductConfigOptionAggrList())) {
            return;
        }
        Map<Long, ProductConfigAggr> productConfigAggrMap = LambdaUtil.toKeyMap(event.getProductConfigAggrList(), ProductConfigAggr::getId);
        event.getProductConfigOptionAggrList().forEach(aggr -> {
            try {
                ProductConfigAggr productConfigAggr = productConfigAggrMap.get(aggr.getPcId());
                if (productConfigAggr == null) {
                    throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NOT_EXIST);
                }
                SyncProductConfigKafkaCmd cmd = new SyncProductConfigKafkaCmd();
                cmd.setPcId(productConfigAggr.getPcId());
                cmd.setSyncProductConfigOptionKafkaDto(buildSyncProductConfigOptionKafkaDto(productConfigAggr, aggr));
                kafkaSender.sendSyncProductConfig(cmd);
            } catch (Exception e) {
                log.error("Kafka sendSyncProductConfig error", e);
            }
        });*/
        return;
    }

    /*private SyncProductConfigOptionKafkaDto buildSyncProductConfigOptionKafkaDto(ProductConfigAggr productConfigAggr, ProductConfigOptionAggr productConfigOptionAggr) {
        SyncProductConfigOptionKafkaDto kafkaDto = new SyncProductConfigOptionKafkaDto();
        kafkaDto.setPcId(productConfigAggr.getPcId());
        kafkaDto.setOptionCode(productConfigOptionAggr.getOptionCode());
        kafkaDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
        kafkaDto.setSelect(productConfigOptionAggr.isSelect());
        return kafkaDto;
    }*/

}
