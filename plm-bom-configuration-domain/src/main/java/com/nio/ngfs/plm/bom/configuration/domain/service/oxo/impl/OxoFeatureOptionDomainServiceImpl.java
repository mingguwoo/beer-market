package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.OxoPackageInfoEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoFeatureOptionDomainServiceImpl implements OxoFeatureOptionDomainService {

    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;

    private final OxoOptionPackageRepository oxoOptionPackageRepository;

    @Override
    public void renewSortFeatureOption(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, String targetFeatureCode, List<String> moveFeatureCodeList) {
        if (CollectionUtils.isEmpty(oxoFeatureOptionAggrList)) {
            return;
        }
        Set<String> existFeatureCodeSet = oxoFeatureOptionAggrList.stream().map(OxoFeatureOptionAggr::getFeatureCode).collect(Collectors.toSet());
        if (!existFeatureCodeSet.containsAll(moveFeatureCodeList)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        // 排序
        oxoFeatureOptionAggrList = oxoFeatureOptionAggrList.stream().sorted(OxoFeatureOptionAggr::compareTo).toList();
        List<String> sortFeatureCodeList = LambdaUtil.map(oxoFeatureOptionAggrList, OxoFeatureOptionAggr::getFeatureCode);
        Set<String> moveFeatureCodeSet = Sets.newHashSet(moveFeatureCodeList);
        // 重排序Feature Code
        List<String> newSortFeatureCodeList = Lists.newArrayList();
        sortFeatureCodeList.forEach(featureCode -> {
            if (moveFeatureCodeSet.contains(featureCode)) {
                return;
            }
            newSortFeatureCodeList.add(featureCode);
            if (Objects.equals(featureCode, targetFeatureCode)) {
                newSortFeatureCodeList.addAll(moveFeatureCodeList);
            }
        });
        // 更新排序值sort
        Map<String, OxoFeatureOptionAggr> oxoFeatureOptionAggrMap = LambdaUtil.toKeyMap(oxoFeatureOptionAggrList, OxoFeatureOptionAggr::getFeatureCode);
        for (int i = 0; i < newSortFeatureCodeList.size(); i++) {
            oxoFeatureOptionAggrMap.get(newSortFeatureCodeList.get(i)).renewSort(i);
        }
    }

    @Override
    public void checkFeatureOptionDelete(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        featureOptionAggrList.forEach(featureOptionAggr -> {
            if (!featureOptionAggr.canDelete()) {
                throw new BusinessException(ConfigErrorCode.OXO_FEATURE_OPTION_CAN_NOT_DELETE);
            }
        });
    }

    @Override
    public List<OxoOptionPackageAggr> filter(List<OxoOptionPackageAggr> points, List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows) {
        List<Long> repeatRows = driveHandRegionSalesVersionRows.stream().map(row -> row.getId()).collect(Collectors.toList());
        return points.stream().filter(point -> !repeatRows.contains(point.getFeatureOptionId())).toList();
    }


    /**
     * OXO中是否存在
     * 在所有Base Vehicle下打点都为“-”的Option（通过Delete Code删除的Option排除在外）
     */
    @Override
    public List<String> checkOxoBasicVehicleOptions(String modelCode) {

        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs =
                oxoFeatureOptionRepository.queryFeatureListsByModelAndSortDelete(modelCode);

        if (CollectionUtils.isEmpty(oxoFeatureOptionAggrs)) {
            return Lists.newArrayList();
        }

        //行信息
        List<Long> rowIds = oxoFeatureOptionAggrs.stream().map(OxoFeatureOptionAggr::getId).distinct().toList();

        // 获取打点信息
        List<OxoOptionPackageAggr> optionPackages = oxoOptionPackageRepository.queryByBaseVehicleIds(rowIds);

        List<String> optionCodes = Lists.newArrayList();

        optionPackages.stream().collect(Collectors.groupingBy(OxoOptionPackageAggr::getFeatureOptionId)).forEach((k, v) -> {
            if (v.stream().allMatch(x -> StringUtils.equals(x.getPackageCode(), OxoPackageInfoEnum.UNAVAILABLE.getCode()))) {
                optionCodes.add(
                        oxoFeatureOptionAggrs.stream().filter(x -> Objects.equals(x.getId(), k))
                                .findFirst().orElse(new OxoFeatureOptionAggr()).getFeatureCode());
            }
        });

        return optionCodes;
    }

}