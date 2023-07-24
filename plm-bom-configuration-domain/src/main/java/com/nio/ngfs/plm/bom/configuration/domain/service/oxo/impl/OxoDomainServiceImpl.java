package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.bom.share.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.CommonRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.MatrixRuleQueryDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
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

    private final FeatureRepository featureRepository;
    private final CommonRepository commonRepository;



    @Override
    public PageData<OxoChangeLogRespDto> queryChangeLog(OxoBaseCmd oxoBaseCmd) {
        return null;
    }

    @Override
    public void compareExport(OxoCompareCmd compareCmd, HttpServletResponse response) {

    }

//    @Override
//    public void export(OxoListCmd cmd, HttpServletResponse response) {
//
//    }
//
//    @Override
//    public OxoListsRespDto compare(OxoCompareCmd compareCmd) {
//        return null;
//    }


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

    @Override
    public void renewSort(OxoSnapshotCmd cmd) {

    }

    @Override
    public Object querySortFeatureList(OxoSnapshotCmd cmd) {
        return null;
    }
}
