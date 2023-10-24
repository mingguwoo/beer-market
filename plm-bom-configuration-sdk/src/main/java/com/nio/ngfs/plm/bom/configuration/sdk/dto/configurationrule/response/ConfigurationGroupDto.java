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
public class ConfigurationGroupDto implements Dto {

    private Long Id;

    private String chineseName;

    private String displayName;

    private int purpose;

    private String definedBy;

    private String description;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;

    private List<ConfigurationRuleDto> rule = new ArrayList<>();
}
