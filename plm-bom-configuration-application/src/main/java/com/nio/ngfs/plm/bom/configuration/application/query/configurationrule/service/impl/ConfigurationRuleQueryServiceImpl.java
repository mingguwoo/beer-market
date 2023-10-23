package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.impl;

import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bill.wang
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class ConfigurationRuleQueryServiceImpl implements ConfigurationRuleQueryService {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;
    @Override
    public Map<String, BomsFeatureLibraryEntity> queryFeatureOptionMap() {
        Map<String,BomsFeatureLibraryEntity> respMap = new HashMap<>();
        List<BomsFeatureLibraryEntity> entityList = bomsFeatureLibraryDao.queryAll();
        entityList.forEach(entity->{
            respMap.put(entity.getFeatureCode(),entity);
        });
        return respMap;
    }
}
