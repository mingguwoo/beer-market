package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Data
public class SyncDeletePcDto {

    private String pcId;

    private String model;

    private String modelYear;

}
