package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Data
public class QueryConfigurationRuleRespDto implements Dto {

    private List<ConfigurationGroupDto> group = new ArrayList<>();

}
