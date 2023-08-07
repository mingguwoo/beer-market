package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
public interface OxoOptionPackageApplicationService {


    /**
     * 添加 打点信息
     */
    void  insertOxoOptionPackageDefault(List<OxoFeatureOptionAggr> oxoFeatureOptions,
                                        String modelCode,
                                        String userName);
}
