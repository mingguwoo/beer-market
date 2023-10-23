package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Data
public class CriteriaDto implements Dto {

    private String featureCode;

    private String featureDisplayName;

    private String featureChineseName;

    private String optionCode;

    private String optionChineseName;

    private String optionDisplayName;
}
