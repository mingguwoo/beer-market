package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFeatureOptionRespDto implements Dto {

    private List<String> messageList;

}
