package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.application.command.productconfig.context.EditProductConfigContext;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
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
     * @param updateUser                  更新人
     * @return 更新的ProductConfigOptionAggr列表
     */
    List<ProductConfigOptionAggr> editPcOptionConfig(List<EditProductConfigCmd.PcOptionConfigDto> updatePcOptionConfigList, List<ProductConfigAggr> productConfigAggrList,
                                                     List<ProductConfigOptionAggr> productConfigOptionAggrList, String updateUser);

    /**
     * edit时skipCheck校验
     *
     * @param context 上下文
     */
    void skipCheckBeforeEdit(EditProductConfigContext context);

    /**
     * 根据Product Context勾选状态进行编辑校验
     *
     * @param context 上下文
     */
    void checkEditByProductContextSelect(EditProductConfigContext context);

    /**
     * 处理初始化勾选完成
     *
     * @param context 上下文
     */
    void handleCompleteInitSelect(EditProductConfigContext context);

}
