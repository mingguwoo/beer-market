package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import lombok.Data;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class RuleViewCheckRespDto {

    /**
     * 针对硬校验不通过的Rule条目，采用红底色高亮显示
     */
    private List<Long> redRuleIds;

    /**
     * 针对软校验不通过的Rule条目，采用黄底色高亮显示
     */
    private List<Long> yellowRuleIds;


}
