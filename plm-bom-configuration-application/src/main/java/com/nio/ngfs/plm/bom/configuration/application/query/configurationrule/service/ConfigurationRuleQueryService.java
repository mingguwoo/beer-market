package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;

import java.util.Map;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
public interface ConfigurationRuleQueryService {

    /**
     * 查询获取全部featureCode optionCode和feature option的对应关系
     * @return
     */
    Map<String, BomsFeatureLibraryEntity> queryFeatureOptionMap();
}
