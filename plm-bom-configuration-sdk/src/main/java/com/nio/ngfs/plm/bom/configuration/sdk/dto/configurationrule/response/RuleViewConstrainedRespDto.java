package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class RuleViewConstrainedRespDto implements Serializable {


    private String constrainedFeatureCode;

    private String constrainedFeatureName;


    private List<RuleViewConstrainedOption> constrainedOptionList;


    @Getter
    @Setter
    @ToString
    public static class RuleViewConstrainedOption {

        private String constrainedOptionCode;

        private String constrainedOptionName;

        private List<RulePackageInfo> packageCodes;
    }

    @Getter
    @Setter
    @ToString
    public static class RulePackageInfo {

        private Long id;

        private String packageCode;

    }

}
