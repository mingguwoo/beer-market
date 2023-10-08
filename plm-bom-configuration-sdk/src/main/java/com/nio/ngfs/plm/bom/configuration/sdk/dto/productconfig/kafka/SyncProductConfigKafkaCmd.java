package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
@NoArgsConstructor
public class SyncProductConfigKafkaCmd implements Cmd {

    private String pcId;

    private SyncProductConfigKafkaDto syncProductConfigKafkaDto;

    private SyncProductConfigOptionKafkaDto syncProductConfigOptionKafkaDto;

}
