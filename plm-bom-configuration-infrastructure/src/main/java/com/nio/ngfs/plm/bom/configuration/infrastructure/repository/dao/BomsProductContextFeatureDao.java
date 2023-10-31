package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextFeatureEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
public interface BomsProductContextFeatureDao extends IService<BomsProductContextFeatureEntity> {

    /**
     * 根据modelCode获取Product Context Feature数据
     */
    List<BomsProductContextFeatureEntity> queryByModelCode(String modelCode);

    /**
     * 获取下发用全量数据
     * @return
     */
    List<BomsProductContextFeatureEntity> queryAll();
}
