package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/20
 */
@Data
public class CheckRuleReleaseAvailableRespDto implements Dto {
    private boolean isAvailable = false;

}
