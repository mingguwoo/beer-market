package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/10/8
 */
@Data
public class SyncProductConfigOptionKafkaDto implements Dto {

    private String pcId;

    private String optionCode;

    private String featureCode;

    private boolean select;

}
