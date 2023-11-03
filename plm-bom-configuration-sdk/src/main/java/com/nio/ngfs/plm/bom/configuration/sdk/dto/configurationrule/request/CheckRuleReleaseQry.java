package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/10/20
 */
@Data
public class CheckRuleReleaseQry implements Qry {

    private Long id;

    private String ruleNumber;
}
