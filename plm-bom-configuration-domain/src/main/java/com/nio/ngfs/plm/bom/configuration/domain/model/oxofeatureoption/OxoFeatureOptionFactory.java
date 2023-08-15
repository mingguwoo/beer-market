package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public class OxoFeatureOptionFactory {


    public static List<OxoFeatureOptionAggr> buildOxoRowInfoAggrs(OxoAddCmd cmd){

        List<OxoAddCmd.OxoFeatureOption> featureOptions =  cmd.getOxoAdds();

        String modelCode= cmd.getModelCode();
        String userName= cmd.getUserName();

        List<OxoFeatureOptionAggr> oxoRowInfoAggrs= Lists.newArrayList();

        featureOptions.forEach(featureOption->{

            OxoFeatureOptionAggr rowInfoAggr=new OxoFeatureOptionAggr();
            rowInfoAggr.setModelCode(modelCode);
            rowInfoAggr.setCreateUser(userName);
            rowInfoAggr.setUpdateUser(userName);
            rowInfoAggr.setRuleCheck(featureOption.getRuleCheck());
            rowInfoAggr.setType(FeatureTypeEnum.FEATURE.getType());
            rowInfoAggr.setFeatureCode(featureOption.getFeatureCode());
            oxoRowInfoAggrs.add(rowInfoAggr);

            featureOption.getOptionCodes().forEach(optionCode->{

                OxoFeatureOptionAggr optionRowInfo=new OxoFeatureOptionAggr();
                optionRowInfo.setModelCode(modelCode);
                optionRowInfo.setCreateUser(userName);
                optionRowInfo.setUpdateUser(userName);
                optionRowInfo.setRuleCheck(optionCode.getRuleCheck());
                optionRowInfo.setType(FeatureTypeEnum.OPTION.getType());
                optionRowInfo.setFeatureCode(optionCode.getOptionCode());
                oxoRowInfoAggrs.add(optionRowInfo);
            });

        });

        return oxoRowInfoAggrs;
    }
}
