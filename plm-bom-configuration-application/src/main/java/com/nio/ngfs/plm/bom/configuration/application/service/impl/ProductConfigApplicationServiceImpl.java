package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject.BasedOnBaseVehicleFeature;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.domainobject.BasedOnBaseVehicleOption;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Service
@RequiredArgsConstructor
public class ProductConfigApplicationServiceImpl implements ProductConfigApplicationService {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigOptionRepository productConfigOptionRepository;
    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;
    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final ProductConfigOptionDomainService productConfigOptionDomainService;

    @Override
    public List<ProductConfigOptionAggr> copyProductConfigOptionByPc(ProductConfigAggr productConfigAggr) {
        if (StringUtils.isBlank(productConfigAggr.getBasedOnPcId())) {
            return Collections.emptyList();
        }
        ProductConfigAggr basedOnPc = productConfigRepository.find(productConfigAggr.getBasedOnPcId());
        if (basedOnPc == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_PC_NOT_EXIST);
        }
        List<ProductConfigOptionAggr> productConfigOptionAggrList = productConfigOptionRepository.queryByPcId(basedOnPc.getId());
        // copy Based On PC的Code勾选（只copy打勾的）
        return LambdaUtil.map(productConfigOptionAggrList, ProductConfigOptionAggr::isSelect, i -> ProductConfigOptionFactory.createFromPc(productConfigAggr.getId(), i,
                productConfigAggr.getCreateUser()));
    }

    @Override
    public List<ProductConfigOptionAggr> copyProductConfigOptionByBaseVehicle(ProductConfigAggr productConfigAggr) {
        if (productConfigAggr.getBasedOnBaseVehicleId() == null) {
            return Collections.emptyList();
        }
        // 查询Based On OXO发布版本
        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotRepository.find(productConfigAggr.getOxoVersionSnapshotId());
        // copy OXO发布版本Base Vehicle的Feature/Option列表
        List<BasedOnBaseVehicleFeature> baseVehicleFeatureList = copyOxoBaseVehicleFeatureOptionList(productConfigAggr, oxoVersionSnapshotAggr);
        List<ProductConfigOptionAggr> basedOnBaseVehicleOptionAggrList = productConfigOptionDomainService.copyFromBaseVehicle(productConfigAggr.getId(), baseVehicleFeatureList,
                productConfigAggr.getCreateUser());
        // 没有情况4，直接初始化完成
        if (basedOnBaseVehicleOptionAggrList.stream().noneMatch(ProductConfigOptionAggr::isSelectCanEdit)) {
            productConfigAggr.completeInitSelect();
        }
        return basedOnBaseVehicleOptionAggrList;
    }

    /**
     * copy OXO发布版本Base Vehicle的Feature/Option列表
     */
    private List<BasedOnBaseVehicleFeature> copyOxoBaseVehicleFeatureOptionList(ProductConfigAggr productConfigAggr, OxoVersionSnapshotAggr oxoVersionSnapshotAggr) {
        if (oxoVersionSnapshotAggr == null) {
            throw new BusinessException(ConfigErrorCode.OXO_VERSION_SNAPSHOT_NOT_EXIST);
        }
        OxoListRespDto oxoListRespDto = oxoVersionSnapshotDomainService.resolveSnapShot(oxoVersionSnapshotAggr.getOxoSnapshot());
        // 只copy Catalog类型为Engineering的打点
        return LambdaUtil.map(oxoListRespDto.getOxoRowsResps(), i -> Objects.equals(i.getCatalog(), FeatureCatalogEnum.ENGINEERING.getCatalog()), row -> {
            BasedOnBaseVehicleFeature baseVehicleFeature = new BasedOnBaseVehicleFeature();
            baseVehicleFeature.setFeatureCode(row.getFeatureCode());
            baseVehicleFeature.setOptionList(LambdaUtil.map(row.getOptions(), option -> {
                BasedOnBaseVehicleOption baseVehicleOption = new BasedOnBaseVehicleOption();
                baseVehicleOption.setOptionCode(option.getFeatureCode());
                baseVehicleOption.setFeatureCode(row.getFeatureCode());
                // 筛选指定BaseVehicleId
                baseVehicleOption.setPackageCode(Optional.ofNullable(option.getPackInfos()).orElse(Lists.newArrayList()).stream()
                        .filter(i -> Objects.equals(i.getHeadId(), productConfigAggr.getBasedOnBaseVehicleId()))
                        .findFirst().map(OxoEditCmd::getPackageCode).orElse(null));
                if (StringUtils.isBlank(baseVehicleOption.getPackageCode())) {
                    return null;
                }
                return baseVehicleOption;
            }));
            if (CollectionUtils.isEmpty(baseVehicleFeature.getOptionList())) {
                return null;
            }
            return baseVehicleFeature;
        });
    }

}
