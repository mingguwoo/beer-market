package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/23
 */
@Data
public class ReleaseRuleCmd implements Cmd {

    @NotEmpty(message = "Rule Id List is empty")
    private List<Long> ruleIdList;

}
