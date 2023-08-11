package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    @Override
    public List<ProductConfigOptionAggr> copyProductConfigOptionByPc(ProductConfigAggr productConfigAggr) {
        if (StringUtils.isBlank(productConfigAggr.getBasedOnPcId())) {
            return Collections.emptyList();
        }
        ProductConfigAggr basedOnPc = productConfigRepository.find(productConfigAggr.getBasedOnPcId());
        if (basedOnPc == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_PC_NOT_EXIST);
        }
        List<ProductConfigOptionAggr> productConfigOptionAggrList = productConfigOptionRepository.queryByPcId(basedOnPc.getPcId());
        return LambdaUtil.map(productConfigOptionAggrList, i -> ProductConfigOptionFactory.create(productConfigAggr.getPcId(), i));
    }

    @Override
    public List<ProductConfigOptionAggr> copyProductConfigOptionByBaseVehicle(ProductConfigAggr productConfigAggr) {
        if (productConfigAggr.getBasedOnBaseVehicleId() == null) {
            return Collections.emptyList();
        }
        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotRepository.find(productConfigAggr.getOxoVersionSnapshotId());
        return null;
    }

}
