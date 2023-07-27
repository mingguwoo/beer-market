package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.GetBaseVehicleOptionsQry;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoFeatureOptionDao extends IService<BomsOxoFeatureOptionEntity> {

    /**
     * 根据Model Code 和 Feature Code去批量查询
     *
     * @param featureList
     * @param qry
     * @return BomsOxoFeatureOptionEntity列表
     */
    List<BomsOxoFeatureOptionEntity> getBaseVehicleOptions(List<String> featureList, String modelCode);

}
