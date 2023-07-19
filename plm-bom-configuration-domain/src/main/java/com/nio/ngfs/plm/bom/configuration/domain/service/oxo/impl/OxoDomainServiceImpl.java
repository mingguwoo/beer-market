package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.BrandEnum;
import com.nio.ngfs.plm.bom.configuration.domain.facade.AdministratorDetailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.CommonRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.common.MatrixRuleQueryDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.PageData;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoChangeLogRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListsRespDto;
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
    
    private final AdministratorDetailFacade administratorDetailFacade;
    private final OxoRepository oxoRepository;
    private final FeatureRepository featureRepository;
    private final CommonRepository commonRepository;


    /**
     * 查询车型下版本
     *
     * @param cmd
     * @return
     */
    @Override
    public List<String> queryVersion(OxoBaseCmd cmd) {
        String userName = cmd.getUserName();
        String modelCode = cmd.getModelCode();

        // 获取人员角色
        List<String> roleNames = administratorDetailFacade.queryRoleNamesByUserName(userName);

        // 查询oxo版本
        List<OxoVersionSnapshotAggr> oxoVersionSnapshotAggrs =
                oxoRepository.queryOxoVersionSnapshotLists(modelCode);

        if (CollectionUtils.isEmpty(oxoVersionSnapshotAggrs)) {
            return Lists.newArrayList();
        }

        List<String> revisions = Lists.newArrayList();

        List<String> roleRevisions = oxoVersionSnapshotAggrs.stream()
                .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.FORMAL.getCode()))
                .map(OxoVersionSnapshotAggr::getVersion).toList().stream()
                .sorted(Comparator.reverseOrder()).toList();

        /**下拉框展示的值默认为Working（即展示OXO Working版本数据），下拉框可选值：Working版本、Formal版本、最新Informal版本*/
        if (roleNames.contains(ConfigConstants.CONFIG_ADMIN)) {

            revisions.add(ConfigConstants.WORKING);

            // Formal版本
            revisions.addAll(roleRevisions);

            //最新Informal版本
            revisions.add(oxoVersionSnapshotAggrs.stream()
                    .filter(x -> StringUtils.equals(x.getType(), OxoSnapshotEnum.INFORMAL.getCode()))
                    .map(OxoVersionSnapshotAggr::getVersion).toList().stream().min(Comparator.reverseOrder()).orElse(StringUtils.EMPTY));
        }

        /**下拉框展示的值默认为最新Formal发布版本（即展示OXO最新正式发布版本数据），
         * 下拉框可选值为所有Formal Version(不包含Working版本和Informal版本)*/
        else if (roleNames.contains(ConfigConstants.CONFIG_USER) ||
                roleNames.contains(ConfigConstants.FEATURE_LIBRARY)) {
            revisions.addAll(roleRevisions);
        }

        return revisions.stream().filter(StringUtils::isNotBlank).distinct().toList();
    }

    @Override
    public OxoListsRespDto queryList(OxoListCmd cmd) {
        return null;
    }

    @Override
    public PageData<OxoChangeLogRespDto> queryChangeLog(OxoBaseCmd oxoBaseCmd) {
        return null;
    }

    @Override
    public void compareExport(OxoCompareCmd compareCmd, HttpServletResponse response) {

    }

    @Override
    public void export(OxoListCmd cmd, HttpServletResponse response) {

    }

    @Override
    public OxoListsRespDto compare(OxoCompareCmd compareCmd) {
        return null;
    }


    /**
     * @return
     */
    @Override
    public OxoAddCmd queryFeatureList(OxoBaseCmd cmd) {


        List<FeatureAggr> featureAggrs = featureRepository.queryFeatureOptionLists(cmd.getModelCode());

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
