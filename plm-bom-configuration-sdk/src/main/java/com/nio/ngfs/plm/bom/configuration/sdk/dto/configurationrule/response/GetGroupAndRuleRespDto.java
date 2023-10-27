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

        private boolean canDelete;

        private List<RuleRowColumnConfig> configList;

    }

    @Data
    public static class RuleRowColumnConfig {

        private Integer uniqueId;

        private String drivingOptionCode;

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

        private Integer uniqueId;

        private String optionCode;

        private String chineseName;

    }

}
