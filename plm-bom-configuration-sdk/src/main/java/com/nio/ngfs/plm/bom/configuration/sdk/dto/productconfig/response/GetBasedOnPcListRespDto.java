package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Data
public class GetBasedOnPcListRespDto implements Dto {

    private String pcId;

    private String pcName;

}
