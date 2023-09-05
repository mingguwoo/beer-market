package com.nio.ngfs.plm.bom.configuration.domain.service.productconfig;

import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
public interface ProductConfigDomainService {

    /**
     * 获取并校验聚合根
     *
     * @param pcId PC Id
     * @return 聚合根
     */
    ProductConfigAggr getAndCheckAggr(String pcId);

    /**
     * 生成PC Id
     *
     * @param model     车型
     * @param modelYear Model Year
     * @return PC Id
     */
    String generatePcId(String model, String modelYear);

    /**
     * 校验PC Name是否唯一
     *
     * @param productConfigAggr 聚合根
     */
    void checkPcNameUnique(ProductConfigAggr productConfigAggr);

    /**
     * 改变PC的skipCheck开关
     *
     * @param pcSkipCheckMap PC的skipCheck集合
     * @param updateUser     更新人
     * @return 聚合根列表
     */
    List<ProductConfigAggr> changePcSkipCheck(Map<String, Boolean> pcSkipCheckMap, String updateUser);

    /**
     * 检查是否完成初始化勾选
     *
     * @param productConfigAggrList       PC列表
     * @param productConfigOptionAggrList Product Config勾选列表
     */
    void checkCompleteInitSelect(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList);

}
