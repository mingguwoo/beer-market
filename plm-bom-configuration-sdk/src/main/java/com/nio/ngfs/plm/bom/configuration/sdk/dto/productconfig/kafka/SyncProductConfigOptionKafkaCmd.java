package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
public class SyncProductConfigOptionKafkaCmd implements Cmd {

    private String pcId;

    private String optionCode;

    private String featureCode;

    private boolean select;

}
