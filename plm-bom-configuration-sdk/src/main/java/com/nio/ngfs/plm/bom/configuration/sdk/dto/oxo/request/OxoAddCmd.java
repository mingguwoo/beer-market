package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class OxoAddCmd extends OxoBaseCmd {

    private List<OxoFeatureOption> oxoAdds;

    @Getter
    @Setter
    @ToString
    public static class OxoFeatureOption {

        private String featureCode;

        private List<OxoOption> optionCodes;


        private String ruleCheck = "Y";

        private String displayName;

        private String chineseName;

        private String catalog;



    }


    @Getter
    @Setter
    @ToString
    public static class OxoOption {

        private String optionCode;


        private String ruleCheck = "Y";

        private String displayName;

        private String chineseName;
    }


}
