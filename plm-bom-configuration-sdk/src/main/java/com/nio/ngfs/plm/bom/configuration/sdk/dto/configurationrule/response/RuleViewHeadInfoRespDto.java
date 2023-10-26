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
public class RuleViewHeadInfoRespDto implements Serializable {


    private String driveFeatureCode;


    private String driveFeatureName;


    private List<DriveOptionInfo> optionHeadList;


    @Getter
    @Setter
    @ToString
    public static class DriveOptionInfo {


        private String changeType;

        private String effIn;

        private String effOut;

        private String driveOptionCode;

        private String driveOptionName;

        private String revision;

        private Long  ruleId;
    }


}
