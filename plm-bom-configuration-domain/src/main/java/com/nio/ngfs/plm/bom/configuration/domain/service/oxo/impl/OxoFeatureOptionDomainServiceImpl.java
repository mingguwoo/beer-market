package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
            if (oxoFeatureOptionAggrList.get(0).isFeature()) {
                throw new BusinessException(ConfigErrorCode.OXO_RENEW_SORT_FEATURE_NOT_THE_SAME_GROUP);
            } else {
                throw new BusinessException(ConfigErrorCode.OXO_RENEW_SORT_OPTION_NOT_THE_SAME_GROUP);
            }
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
            oxoFeatureOptionAggrMap.get(newSortFeatureCodeList.get(i)).renewSort(i + 1);
        }
    }

    @Override
    public void checkFeatureOptionDelete(List<String> featureCodeList, List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (featureCodeList.size() != featureOptionAggrList.size()) {
            throw new BusinessException(ConfigErrorCode.OXO_FEATURE_OPTION_IS_ALREADY_DELETE);
        }
        featureOptionAggrList.forEach(featureOptionAggr -> {
            if (!featureOptionAggr.canDelete()) {
                throw new BusinessException(ConfigErrorCode.OXO_FEATURE_OPTION_CAN_NOT_DELETE);
            }
        });
    }

    @Override
    public List<OxoFeatureOptionAggr> removeRepeatFeatureOption(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (CollectionUtils.isEmpty(featureOptionAggrList)) {
            return Lists.newArrayList();
        }
        Set<String> childOptionCodeSet = Sets.newHashSet();
        featureOptionAggrList.stream().filter(OxoFeatureOptionAggr::isFeature).forEach(feature -> {
            if (CollectionUtils.isNotEmpty(feature.getChildren())) {
                feature.getChildren().forEach(option -> childOptionCodeSet.add(option.getFeatureCode()));
            }
        });
        // 去除重复的Option Code
        return featureOptionAggrList.stream().filter(i -> i.isFeature() || !childOptionCodeSet.contains(i.getFeatureCode())).toList();
    }

    @Override
    public List<OxoOptionPackageAggr> filterRepeatCopyfromPoints(List<OxoOptionPackageAggr> points, List<OxoFeatureOptionAggr> driveHandRegionSalesVersionRows) {
        Set<Long> repeatRows = driveHandRegionSalesVersionRows.stream().map(row -> row.getId()).collect(Collectors.toSet());
        return points.stream().filter(point -> !repeatRows.contains(point.getFeatureOptionId())).toList();
    }
}
