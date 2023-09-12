package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/25
 */
@Data
public class EditProductConfigRespDto implements Dto {

    private List<PcMessageDto> pcMessageList;

    @Data
    public static class PcMessageDto {

        private String pcId;

        private List<String> messageList;

    }

}
