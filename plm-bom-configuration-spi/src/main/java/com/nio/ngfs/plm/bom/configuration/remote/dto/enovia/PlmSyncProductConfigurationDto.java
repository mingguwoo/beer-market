package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Data
public class PlmSyncProductConfigurationDto {

    private String pcId;

    private String name;

    private String description;

    private String marketingName;

    private String model;

    private String modelYear;

    private String revision;

    private String owner;

    private String brandName;

}
