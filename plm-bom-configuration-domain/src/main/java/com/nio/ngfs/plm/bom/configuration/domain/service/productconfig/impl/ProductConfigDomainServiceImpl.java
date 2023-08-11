package com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Service
@RequiredArgsConstructor
public class ProductConfigDomainServiceImpl implements ProductConfigDomainService {

    private final ProductConfigRepository productConfigRepository;

    @Override
    public String generatePcId(String model, String modelYear) {
        String pcIdPrefix = model + " " + modelYear + "-";
        ProductConfigAggr productConfigAggr = productConfigRepository.queryLastPcByModelAndModelYear(model, modelYear);
        if (productConfigAggr == null) {
            return pcIdPrefix + "0001";
        }
        String number = productConfigAggr.getPcId().substring(pcIdPrefix.length());
        if (!RegexUtil.isMatchPcIdSuffix(number)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_ID_FORMAT_ERROR);
        }
        // 去除前面的字符0
        while (number.startsWith(CommonConstants.STR_ZERO)) {
            number = number.substring(1);
        }
        return String.format("%s%04d", pcIdPrefix, Integer.parseInt(number) + 1);
    }

    @Override
    public void checkPcNameUnique(ProductConfigAggr productConfigAggr) {
        ProductConfigAggr existProductConfigAggr = productConfigRepository.getByName(productConfigAggr.getName());
        if (existProductConfigAggr != null) {
            if (Objects.equals(productConfigAggr.getId(), existProductConfigAggr.getId())) {
                // id相同，代表同一条记录，忽略
                return;
            }
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NAME_REPEAT);
        }
    }

}