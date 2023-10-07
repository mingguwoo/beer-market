package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Data
public class SyncProductContextFeatureKafkaCmd {

        private String featureCode;

        List<SyncProductContextOptionKafkaCmd> option;
}
