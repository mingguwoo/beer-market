package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditProductConfigRespDto implements Dto {

    private List<String> messageList;

}
