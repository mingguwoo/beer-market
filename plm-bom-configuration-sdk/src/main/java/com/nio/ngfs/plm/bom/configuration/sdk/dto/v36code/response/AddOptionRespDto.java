package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
@NoArgsConstructor
public class AddOptionRespDto implements Dto {

    private Long id;

    private String message;

    public AddOptionRespDto(Long id) {
        this.id = id;
    }

    public AddOptionRespDto(String message) {
        this.message = message;
    }

}
