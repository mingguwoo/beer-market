package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.bom.share.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.CommonRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.MatrixRuleQueryDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Service
@RequiredArgsConstructor
public class OxoDomainServiceImpl implements OxoDomainService {

    private OxoRepository oxoRepository;
    private final CommonRepository commonRepository;


    /**
     * 插入 修改打点信息
     * @param packageInfoAggrs
     */
    @Override
    public void insertOxoOptionPackageInfos(List<OxoPackageInfoAggr> packageInfoAggrs) {
        oxoRepository.insertOxoOptionPackageInfo(packageInfoAggrs);
    }

    /**
     * @return
     */
    @Override
    public OxoAddCmd queryFeatureList(OxoBaseCmd cmd) {


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


    /**
     * 获取email 收件人
     * @return
     */
    @Override
    public List<String> queryEmailGroup() {

        String brandName = ConfigConstants.brandName.get();

        String name = ConfigConstants.OXO_EMAIL_GROUP;
        if (StringUtils.equals(brandName, BrandEnum.ALPS.name())) {
            name = ConfigConstants.OXO_EMAIL_GROUP_ALPS;
        }

        Map<String, String> map = commonRepository.queryMatrixRuleValuesByAbscissaOrOrdinate(new MatrixRuleQueryDo
                (name, "matrix", null, "oxo.email", "PLM.EBOM.PartNumRequest"));

        List<String> list = new LinkedList<>();
        map.forEach((x, y) -> {
            list.add(x);
        });
        return list.stream().distinct().toList();
    }


}
