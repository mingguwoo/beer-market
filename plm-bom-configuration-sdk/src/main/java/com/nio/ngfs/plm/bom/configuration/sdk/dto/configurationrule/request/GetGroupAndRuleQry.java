package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xiaozhou.tu
 * @date 2023/10/27
 */
@Data
public class GetGroupAndRuleQry implements Qry {

    @NotNull(message = "Group Id is null")
    private Long groupId;

}
