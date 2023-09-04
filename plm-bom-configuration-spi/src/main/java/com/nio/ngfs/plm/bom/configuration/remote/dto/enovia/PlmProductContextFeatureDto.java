package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Data
public class PlmProductContextFeatureDto {

    private String featureCode;

    List<PlmProductContextOptionDto> option;
}
