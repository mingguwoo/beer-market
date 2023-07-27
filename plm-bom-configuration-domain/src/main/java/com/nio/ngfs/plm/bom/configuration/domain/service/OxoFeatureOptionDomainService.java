package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoFeatureOptionDomainService {

    /**
     * 查询同一排序分组的Feature/Option
     *
     * @param model             车型
     * @param featureOptionCode Feature/Option Code
     * @return OxoFeatureOptionAggr列表
     */
    List<OxoFeatureOptionAggr> querySameSortGroupFeatureOption(String model, String featureOptionCode);

    /**
     * 重新排序Feature/Option
     *
     * @param oxoFeatureOptionAggrList OxoFeatureOptionAggr列表
     * @param targetFeatureCode        目标Feature Code
     * @param moveFeatureCodeList      移动的Feature Code列表
     */
    void renewSortFeatureOption(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, String targetFeatureCode, List<String> moveFeatureCodeList);

}
