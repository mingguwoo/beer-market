package com.nio.ngfs.plm.bom.configuration.domain.model.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.RuleCheckEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class OxoFactory {



    public static List<OxoRowInfoAggr> buildOxoRowInfoAggrs(OxoAddCmd cmd){

        List<OxoAddCmd.OxoFeatureOption> featureOptions =  cmd.getOxoAdds();

        String modelCode= cmd.getModelCode();
        String userName= cmd.getUserName();

        List<OxoRowInfoAggr> oxoRowInfoAggrs= Lists.newArrayList();

        featureOptions.forEach(featureOption->{

            OxoRowInfoAggr rowInfoAggr=new OxoRowInfoAggr();
            rowInfoAggr.setModelCode(modelCode);
            rowInfoAggr.setCreateUser(userName);
            rowInfoAggr.setUpdateUser(userName);
            rowInfoAggr.setRuleCheck(RuleCheckEnum.Y.getCode());
            rowInfoAggr.setIsHead(1);
            rowInfoAggr.setFeatureCode(featureOption.getFeatureCode());
            oxoRowInfoAggrs.add(rowInfoAggr);

            featureOption.getOptionCodes().forEach(optionCode->{

                OxoRowInfoAggr optionRowInfo=new OxoRowInfoAggr();
                optionRowInfo.setModelCode(modelCode);
                optionRowInfo.setCreateUser(userName);
                optionRowInfo.setUpdateUser(userName);
                optionRowInfo.setRuleCheck(RuleCheckEnum.Y.getCode());
                optionRowInfo.setIsHead(2);
                optionRowInfo.setFeatureCode(optionCode);
                oxoRowInfoAggrs.add(optionRowInfo);
            });

        });

        return oxoRowInfoAggrs;
    }

}
