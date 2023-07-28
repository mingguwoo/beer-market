package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
public interface OxoFeatureOptionApplicationService {

    /**
     * 查询同一排序分组的Feature/Option
     *
     * @param model             车型
     * @param featureOptionCode Feature/Option Code
     * @return OxoFeatureOptionAggr列表
     */
    List<OxoFeatureOptionAggr> querySameSortGroupFeatureOption(String model, String featureOptionCode);

}
