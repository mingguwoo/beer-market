package com.nio.ngfs.plm.bom.configuration.remote.dto.feature;

import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Data
public class PlmFeatureOptionSyncDto {

    private String owner;

    private Feature feature;

    private List<Option> optionList;

    @Data
    public static class Feature {

        private String featureFamily;

        private String type;

        private String featureCode;

        private String selectionType;

        private String catalogue;

        private String featureName;

        private String description;

        private String chineseName;

    }

    @Data
    public static class Option {

        private String type;

        private String optionCode;

        private Integer optionSeq;

        private String optionName;

        private String description;

        private String chineseName;

    }

}
