package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class RuleViewBasicInformationRespDto {




      private String chineseName;


      private String displayName;


      private Integer purpose;


      private String description;


      private List<String> drivingCriterias;



}
