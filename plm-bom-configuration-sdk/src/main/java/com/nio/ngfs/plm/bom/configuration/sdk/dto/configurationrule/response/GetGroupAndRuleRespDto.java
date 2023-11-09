package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/27
 */
@Data
public class GetGroupAndRuleRespDto implements Dto {

    private Long groupId;

    private String chineseName;

    private String displayName;

    private Integer purpose;

    private String definedBy;

    private String description;

    private String drivingFeature;

    private List<String> constrainedFeatureList;

    private List<RuleConstrainedFeature> ruleConstrainedFeatureList;

    private List<RuleDrivingFeature> ruleDrivingFeatureList;

    @Data

    public static class RuleConstrainedFeature {

        private String featureCode;

        private String chineseName;

        private List<RuleConstrainedOption> optionList;

    }

    @Data
    public static class RuleConstrainedOption {

        private String optionCode;

        private String chineseName;

        /**
         * 是否可删除
         */
        private boolean canDelete;

        /**
         * 是否新增
         */
        private boolean add;

        private List<RuleRowColumnConfig> configList;

    }

    @Data
    public static class RuleRowColumnConfig {

        private Long uniqueId;

        private String drivingOptionCode;

        private String drivingFeatureCode;

        private Integer matrixValue;

    }

    @Data
    public static class RuleDrivingFeature {

        private String featureCode;

        private String chineseName;

        private List<RuleDrivingOption> optionList;

    }

    @Data
    public static class RuleDrivingOption {

        private Long uniqueId;

        private String optionCode;

        private String chineseName;

        private String featureCode;

        /**
         * 是否已发布
         */
        private boolean released;

    }

}
