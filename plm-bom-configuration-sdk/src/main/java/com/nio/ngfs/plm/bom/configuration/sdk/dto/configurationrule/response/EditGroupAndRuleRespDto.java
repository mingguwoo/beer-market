package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditGroupAndRuleRespDto implements Dto {

    private List<String> messageList;

}
