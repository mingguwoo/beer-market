package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/10/7
 */
@Data
public class SyncProductContextModelFeatureOptionKafkaCmd implements Cmd {

    private String model;

    private List<SyncProductContextFeatureKafkaCmd> feature;

}
