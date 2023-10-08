package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/7
 */
@Data
public class SyncProductContextKafkaCmd implements Cmd {

    private SyncProductContextModelFeatureKafkaCmd syncProductContextModelFeatureKafkaCmd;

    private SyncProductContextModelFeatureOptionKafkaCmd syncProductContextModelFeatureOptionKafkaCmd;
}
