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
public class RuleViewInfoRespDto {

    /**
     * 基础信息
     */
    public RuleViewBasicInformationRespDto ruleViewBasicInformationRespDto;

    /**
     * 行信息
     */
    public List<RuleViewConstrainedRespDto> ruleViewConstrainedLists;


    /**
     * 头信息
     */
    public RuleViewHeadInfoRespDto headInfoRespDto;


}
