package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface BomsProductConfigDao extends IService<BomsProductConfigEntity> {

    /**
     * 根据pcId查询
     *
     * @param pcId PC Id
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity getByPcId(String pcId);

    /**
     * 根据车型查询
     *
     * @param model 车型
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByModel(String model);

    /**
     * 根据车型和Model Year查询最新的PC
     *
     * @param model     车型
     * @param modelYear Model Year
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity queryLastPcByModelAndModelYear(String model, String modelYear);

    /**
     * 根据Name查询
     *
     * @param name Name
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity getByName(String name);

}
