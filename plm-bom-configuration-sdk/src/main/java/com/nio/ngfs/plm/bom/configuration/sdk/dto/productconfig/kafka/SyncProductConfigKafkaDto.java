package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/10/8
 */
@Data
public class SyncProductConfigKafkaDto implements Dto {

    private String pcId;

    private String name;

    private String model;

    private String modelYear;

    private String marketingName;

    private String description;

    private String brand;

    private String createUser;

}
