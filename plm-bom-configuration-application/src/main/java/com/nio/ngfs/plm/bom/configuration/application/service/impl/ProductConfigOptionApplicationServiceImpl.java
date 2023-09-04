package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditProductConfigCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/9/4
 */
@Service
@RequiredArgsConstructor
public class ProductConfigOptionApplicationServiceImpl implements ProductConfigOptionApplicationService {

    @Override
    public List<ProductConfigOptionAggr> editPcOptionConfig(List<EditProductConfigCmd.PcOptionConfigDto> updatePcOptionConfigList, List<ProductConfigAggr> productConfigAggrList,
                                                            List<ProductConfigOptionAggr> productConfigOptionAggrList, List<ProductContextAggr> productContextAggrList) {
        Map<ProductConfigOptionId, ProductConfigOptionAggr> productConfigOptionAggrMap = LambdaUtil.toKeyMap(productConfigOptionAggrList, ProductConfigOptionAggr::getUniqId);
        return LambdaUtil.map(updatePcOptionConfigList, i -> {
            ProductConfigOptionAggr existProductConfigOptionAggr = productConfigOptionAggrMap.get(new ProductConfigOptionId(i.getPcId(), i.getOptionCode()));
            if (existProductConfigOptionAggr == null) {
                // 新增的勾选记录
                return ProductConfigOptionFactory.create(i.getPcId(), i.getOptionCode(), i.getFeatureCode());
            }
            return existProductConfigOptionAggr;
        });
    }

    private void checkEditPcOptionConfig() {
    }

}
