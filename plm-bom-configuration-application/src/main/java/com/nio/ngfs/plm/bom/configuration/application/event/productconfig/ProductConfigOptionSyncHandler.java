package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.kafka.KafkaSender;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigOptionKafkaCmd;
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
                kafkaSender.sendSyncProductConfig(buildSyncProductConfigOptionKafkaCmd(productConfigAggr, aggr));
            } catch (Exception e) {
                log.error("Kafka sendSyncProductConfig error", e);
            }
        });
    }

    private SyncProductConfigOptionKafkaCmd buildSyncProductConfigOptionKafkaCmd(ProductConfigAggr productConfigAggr, ProductConfigOptionAggr productConfigOptionAggr) {
        SyncProductConfigOptionKafkaCmd kafkaCmd = new SyncProductConfigOptionKafkaCmd();
        kafkaCmd.setPcId(productConfigAggr.getPcId());
        kafkaCmd.setOptionCode(productConfigOptionAggr.getOptionCode());
        kafkaCmd.setFeatureCode(productConfigOptionAggr.getFeatureCode());
        kafkaCmd.setSelect(productConfigOptionAggr.isSelect());
        return kafkaCmd;
    }

}
