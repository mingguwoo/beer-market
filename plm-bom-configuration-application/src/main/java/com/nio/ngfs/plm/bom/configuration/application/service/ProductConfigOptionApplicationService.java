package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditProductConfigCmd;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/4
 */
public interface ProductConfigOptionApplicationService {

    /**
     * 编辑Product Config勾选
     *
     * @param updatePcOptionConfigList    更新的Product Config勾选列表
     * @param productConfigAggrList       PC列表
     * @param productConfigOptionAggrList Product Config勾选列表
     * @param productContextAggrList      Product Context勾选列表
     * @return 更新的ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> editPcOptionConfig(List<EditProductConfigCmd.PcOptionConfigDto> updatePcOptionConfigList, List<ProductConfigAggr> productConfigAggrList,
                                                     List<ProductConfigOptionAggr> productConfigOptionAggrList, List<ProductContextAggr> productContextAggrList);

}
