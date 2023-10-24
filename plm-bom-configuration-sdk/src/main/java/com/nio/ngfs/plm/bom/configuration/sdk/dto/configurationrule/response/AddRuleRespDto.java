package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
@NoArgsConstructor
public class AddRuleRespDto implements Dto {

    private String message;

    private Long groupId;

    public AddRuleRespDto(String message) {
        this.message = message;
    }

    public AddRuleRespDto(Long groupId) {
        this.groupId = groupId;
    }

}
