package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureChangeLogEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
public interface BomsFeatureChangeLogDao extends IService<BomsFeatureChangeLogEntity> {

    /**
     * 根据featureId查询
     *
     * @param featureId featureId
     * @return BomsFeatureChangeLogEntity列表
     */
    List<BomsFeatureChangeLogEntity> queryByFeatureId(Long featureId);

}
