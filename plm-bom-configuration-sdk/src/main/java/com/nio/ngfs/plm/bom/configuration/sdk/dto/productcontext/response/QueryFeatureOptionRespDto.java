package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Data
public class QueryFeatureOptionRespDto implements Dto {

    private String featureCode;

    private String chineseName;

    private String displayName;

    private String catalog;

    private List<OptionItemDto> optionList;

    @Data
    public static class OptionItemDto {

        private String optionCode;

        private String chineseName;

        private String displayName;

    }

}
