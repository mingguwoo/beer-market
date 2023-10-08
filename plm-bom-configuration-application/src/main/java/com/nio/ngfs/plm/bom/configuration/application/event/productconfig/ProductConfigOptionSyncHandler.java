package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.kafka.KafkaSender;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigOptionKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ProductConfig打点同步到3DE
 *
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigOptionSyncHandler implements EventHandler<ProductConfigOptionChangeEvent> {

    private final KafkaSender kafkaSender;

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(@NotNull ProductConfigOptionChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getProductConfigOptionAggrList())) {
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
        });
    }

    private SyncProductConfigOptionKafkaDto buildSyncProductConfigOptionKafkaDto(ProductConfigAggr productConfigAggr, ProductConfigOptionAggr productConfigOptionAggr) {
        SyncProductConfigOptionKafkaDto kafkaDto = new SyncProductConfigOptionKafkaDto();
        kafkaDto.setPcId(productConfigAggr.getPcId());
        kafkaDto.setOptionCode(productConfigOptionAggr.getOptionCode());
        kafkaDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
        kafkaDto.setSelect(productConfigOptionAggr.isSelect());
        return kafkaDto;
    }

}
