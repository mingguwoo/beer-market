package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoFeatureOptionQuery implements Query<OxoBaseCmd, OxoAddCmd> {


    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;

    @Override
    public OxoAddCmd execute(OxoBaseCmd cmd) {
        String modelCode = cmd.getModelCode();

        List<OxoFeatureOptionAggr> featureAggrs = oxoFeatureOptionRepository.queryFeaturesByModel(modelCode);

        if (CollectionUtils.isEmpty(featureAggrs)) {
            return null;
        }

        Map<String, List<OxoFeatureOptionAggr>> stringListMap =
                featureAggrs.stream().sorted(Comparator.comparing(OxoFeatureOptionAggr::getParentFeatureCode))
                        .collect(Collectors.groupingBy(OxoFeatureOptionAggr::getParentFeatureCode));

        List<OxoAddCmd.OxoFeatureOption> optionList = new LinkedList<>();

        stringListMap.forEach((k, v) -> {
            OxoAddCmd.OxoFeatureOption oxoFeatureOption=new OxoAddCmd.OxoFeatureOption();
            oxoFeatureOption.setFeatureCode(k);
            oxoFeatureOption.setOptionCodes(v.stream().map(OxoFeatureOptionAggr::getFeatureCode).distinct(). sorted().toList());
            optionList.add(oxoFeatureOption);
        });
        OxoAddCmd addCmd = new OxoAddCmd();
        addCmd.setOxoAdds(optionList);
        addCmd.setModelCode(cmd.getModelCode());
        addCmd.setUserName(cmd.getUserName());
        return addCmd;
    }
}
