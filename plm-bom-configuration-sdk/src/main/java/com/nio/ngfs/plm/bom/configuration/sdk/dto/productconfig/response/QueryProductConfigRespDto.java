package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/22
 */
@Data
public class QueryProductConfigRespDto implements Dto {

    private List<FeatureDto> featureList;

    private List<PcDto> pcList;

    @Data
    public static class FeatureDto {

        private String featureCode;

        private String group;

        private String displayName;

        private String chineseName;

        private List<OptionDto> optionList;

        public boolean isMatchSearch(String search) {
            if (StringUtils.isBlank(search)) {
                return true;
            }
            return QueryProductConfigRespDto.matchSearch(featureCode, search) ||
                    QueryProductConfigRespDto.matchSearch(displayName, search);
        }

    }

    @Data
    public static class OptionDto {

        private String optionCode;

        private String group;

        private String displayName;

        private String chineseName;

        private List<PcOptionConfigDto> configList;

        public boolean isMatchSearch(String search) {
            if (StringUtils.isBlank(search)) {
                return true;
            }
            return QueryProductConfigRespDto.matchSearch(optionCode, search) ||
                    QueryProductConfigRespDto.matchSearch(displayName, search) ||
                    QueryProductConfigRespDto.matchSearch(chineseName, search);
        }

    }

    @Data
    public static class PcOptionConfigDto {

        private Long id;

        private String pcId;

        /**
         * 是否勾选
         */
        private boolean select;

        /**
         * 是否可编辑
         */
        private boolean selectCanEdit;

        /**
         * 是否置灰
         */
        private boolean setGray;

    }

    @Data
    public static class PcDto {

        private String pcId;

        private String pcName;

        private String model;

        private String modelYear;

        private boolean skipCheck;

    }

    private static boolean matchSearch(String content, String search) {
        return content != null && content.contains(search);
    }

}
