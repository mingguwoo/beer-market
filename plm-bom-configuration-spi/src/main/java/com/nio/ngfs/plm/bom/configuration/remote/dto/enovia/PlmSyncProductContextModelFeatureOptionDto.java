package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/4
 */
@Data
public class PlmSyncProductContextModelFeatureOptionDto {

    private String model;

    private List<PlmProductContextFeatureDto> feature;
}
