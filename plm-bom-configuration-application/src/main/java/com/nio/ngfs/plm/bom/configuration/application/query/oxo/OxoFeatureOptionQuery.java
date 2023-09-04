package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.enums.OxoFeatureOptionTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoFeatureOptionQuery implements Query<OxoBaseCmd, OxoAddCmd> {


    private final OxoFeatureOptionApplicationService featureOptionApplicationService;


    @Override
    public OxoAddCmd execute(OxoBaseCmd cmd) {
        String modelCode = cmd.getModelCode();

        List<FeatureAggr> featureAggrs = featureOptionApplicationService.queryFeaturesByModel(modelCode);

        if (CollectionUtils.isEmpty(featureAggrs)) {
            return null;
        }

        Map<String, List<FeatureAggr>> stringListMap =
                featureAggrs.stream().filter(x -> StringUtils.equals(x.getFeatureId().getType(),
                                OxoFeatureOptionTypeEnum.FEATURE.getType()))
                        .sorted(Comparator.comparing(FeatureAggr::getCatalog).thenComparing(FeatureAggr::getParentFeatureCode).thenComparing(FeatureAggr::getFeatureCode))
                        .collect(Collectors.groupingBy(FeatureAggr::getFeatureCode, LinkedHashMap::new, Collectors.toList()));

        List<OxoAddCmd.OxoFeatureOption> optionList = new LinkedList<>();

        stringListMap.forEach((k, v) -> {
            OxoAddCmd.OxoFeatureOption oxoFeatureOption = new OxoAddCmd.OxoFeatureOption();


            FeatureAggr feature = featureAggrs.stream().filter(y-> StringUtils.equals(y.getFeatureCode(),k)).findFirst().orElse(new FeatureAggr());
            oxoFeatureOption.setDisplayName(feature.getDisplayName());
            oxoFeatureOption.setChineseName(feature.getChineseName());

            oxoFeatureOption.setFeatureCode(k);

            List<FeatureAggr> optionFeatures =
                    featureAggrs.stream().filter(x -> StringUtils.equals(x.getParentFeatureCode(), k)).toList();

            oxoFeatureOption.setOptionCodes(optionFeatures.stream().map(x -> {
                OxoAddCmd.OxoOption oxoOption = new OxoAddCmd.OxoOption();
                oxoOption.setOptionCode(x.getFeatureCode());
                oxoOption.setDisplayName(x.getDisplayName());
                oxoOption.setChineseName(x.getChineseName());
                return oxoOption;
            }).sorted(Comparator.comparing(OxoAddCmd.OxoOption::getOptionCode)).toList());
            optionList.add(oxoFeatureOption);
        });
        OxoAddCmd addCmd = new OxoAddCmd();
        addCmd.setOxoAdds(optionList);
        addCmd.setModelCode(cmd.getModelCode());
        addCmd.setUserName(cmd.getUserName());
        return addCmd;
    }
}
