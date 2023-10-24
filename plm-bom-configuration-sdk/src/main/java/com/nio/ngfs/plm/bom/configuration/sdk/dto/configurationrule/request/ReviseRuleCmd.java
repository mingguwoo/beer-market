package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/24
 */
@Data
public class ReviseRuleCmd implements Cmd {

    private long ruleId;

    private String reviser;

}
