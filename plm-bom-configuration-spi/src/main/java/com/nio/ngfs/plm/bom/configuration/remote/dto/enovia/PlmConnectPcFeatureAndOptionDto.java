package com.nio.ngfs.plm.bom.configuration.remote.dto.enovia;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Data
public class PlmConnectPcFeatureAndOptionDto {

    private String pcId;

    private String optionCode;

    private String featureCode;

}
