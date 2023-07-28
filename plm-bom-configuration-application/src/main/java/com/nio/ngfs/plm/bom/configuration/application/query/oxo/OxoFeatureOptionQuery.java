package com.nio.ngfs.plm.bom.configuration.application.query.oxo;


import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

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
        List<FeatureAggr> featureAggrs = null;

        if (CollectionUtils.isEmpty(featureAggrs)) {
            return null;
        }

        Map<String, List<FeatureAggr>> stringListMap =
                featureAggrs.stream().collect(Collectors.groupingBy(FeatureAggr::getParentFeatureCode));

        List<OxoAddCmd.OxoFeatureOption> optionList = new LinkedList<>();

        stringListMap.forEach((k, v) -> {
            OxoAddCmd.OxoFeatureOption featureOption = new OxoAddCmd.OxoFeatureOption();
            featureOption.setFeatureCode(k);

            List<String> optionCodes = Lists.newArrayList();
            v.stream().forEach(option -> {
                optionCodes.add(option.getFeatureId().getFeatureCode());
            });
            featureOption.setOptionCodes(optionCodes);
            optionList.add(featureOption);
        });
        OxoAddCmd addCmd = new OxoAddCmd();
        addCmd.setOxoAdds(optionList);
        addCmd.setModelCode(cmd.getModelCode());
        addCmd.setUserName(cmd.getUserName());
        return addCmd;
    }
}
