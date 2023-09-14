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
     * 删除
     *
     * @param entity BomsProductConfigEntity
     */
    void remove(BomsProductConfigEntity entity);

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
     * @param modelCode 车型
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByModel(String modelCode);

    /**
     * 根据车型和Model Year查询最新的PC
     *
     * @param modelCode 车型
     * @param modelYear Model Year
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity queryLastPcByModelAndModelYear(String modelCode, String modelYear);

    /**
     * 根据Name查询
     *
     * @param name Name
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity getByName(String name);

    /**
     * 根据车型和Model Year列表查询
     *
     * @param modelCode     车型
     * @param modelYearList Model Year列表
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByModelAndModelYearList(String modelCode, List<String> modelYearList);

    /**
     * 根据PC Id列表查询
     *
     * @param pcIdList      PC Id列表
     * @param includeDelete 是否包含删除
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByPcIdList(List<String> pcIdList, boolean includeDelete);

}
