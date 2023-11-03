package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Data
public class GetPurposeOptionListRespDto implements Dto {

    private Integer code;

    private String purpose;

}
