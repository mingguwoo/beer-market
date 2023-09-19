package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Data
public class PlmSyncProductContextFeatureDto {

    private String featureCode;

    List<PlmSyncProductContextOptionDto> option;

}
