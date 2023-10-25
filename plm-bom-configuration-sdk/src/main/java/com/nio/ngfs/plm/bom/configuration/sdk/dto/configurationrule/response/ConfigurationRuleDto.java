package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Data
public class ConfigurationRuleDto implements Dto {

    private Long id;

    private Long groupId;

    private int purpose;

    private String changeType;

    private String ruleNumber;

    private String ruleType;

    private String effIn;

    private String effOut;

    private String releaseDate;

    private List<CriteriaDto> drivingCriteria = new ArrayList<>();

    private List<CriteriaDto> constrainedCriteria = new ArrayList<>();

    private String ruleRevision;

    private String createUser;

    private String createTime;

    private String updateUser;

    private String updateTime;

    private String status;

    private boolean reviseAvailable = false;

    private Date createTimeForSorted;
}
