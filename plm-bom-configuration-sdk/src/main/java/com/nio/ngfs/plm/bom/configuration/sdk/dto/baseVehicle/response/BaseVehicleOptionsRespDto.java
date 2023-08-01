package com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/1
 */
@Data
public class BaseVehicleOptionsRespDto implements Dto {

    private String optionCode;

    private String chineseName;

    private String englishName;

    private String description;
}
