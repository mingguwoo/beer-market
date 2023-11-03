package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigModelOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigmodeloption.ProductConfigModelOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Service
@RequiredArgsConstructor
public class ProductConfigModelOptionApplicationServiceImpl implements ProductConfigModelOptionApplicationService {

    private final ProductConfigModelOptionRepository productConfigModelOptionRepository;
    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;

    @Override
    public void syncFeatureOptionFromOxoRelease(OxoVersionSnapshotAggr oxoVersionSnapshotAggr) {
        if (oxoVersionSnapshotAggr == null) {
            return;
        }
        // 查询Model下所有的Option行
        List<ProductConfigModelOptionAggr> modelOptionAggrList = productConfigModelOptionRepository.queryByModel(oxoVersionSnapshotAggr.getModelCode());
        OxoListRespDto oxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot(oxoVersionSnapshotAggr.getOxoSnapshot());
        // 过滤OXO Catalog为Engineering的Feature/Option
        List<OptionSyncBo> optionSyncBoList = filterEngineeringFeatureOption(oxoListRespDto);
        // Product Config已存在的Option行
        Set<String> existOptionCodeSet = modelOptionAggrList.stream().map(ProductConfigModelOptionAggr::getOptionCode).collect(Collectors.toSet());
        List<ProductConfigModelOptionAggr> newModelOptionAggrList = optionSyncBoList.stream()
                .filter(i -> !existOptionCodeSet.contains(i.getOptionCode()))
                .map(i -> ProductConfigModelOptionFactory.create(oxoVersionSnapshotAggr.getModelCode(), i.getOptionCode(), i.getFeatureCode(), oxoVersionSnapshotAggr.getCreateUser()))
                .toList();
        productConfigModelOptionRepository.batchSave(newModelOptionAggrList);
    }

    /**
     * 过滤OXO Catalog为Engineering的Feature/Option
     *
     * @param oxoListRespDto oxoListRespDto
     * @return OptionSyncBo列表
     */
    private List<OptionSyncBo> filterEngineeringFeatureOption(OxoListRespDto oxoListRespDto) {
        if (oxoListRespDto == null || CollectionUtils.isEmpty(oxoListRespDto.getOxoRowsResps())) {
            return Collections.emptyList();
        }
        List<OptionSyncBo> optionSyncBoList = Lists.newArrayList();
        oxoListRespDto.getOxoRowsResps().forEach(feature -> {
            if (CollectionUtils.isNotEmpty(feature.getOptions())) {
                feature.getOptions().forEach(option -> {
                    // 过滤Catalog为Engineering的Feature/Option
                    if (!Objects.equals(FeatureCatalogEnum.ENGINEERING.getCatalog(), option.getCatalog())) {
                        return;
                    }
                    optionSyncBoList.add(new OptionSyncBo(option.getFeatureCode(), feature.getFeatureCode()));
                });
            }
        });
        return optionSyncBoList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OptionSyncBo {

        private String optionCode;

        private String featureCode;

    }

}
