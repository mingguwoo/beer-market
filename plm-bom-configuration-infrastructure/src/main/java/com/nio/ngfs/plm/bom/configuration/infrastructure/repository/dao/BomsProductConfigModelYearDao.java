package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelYearEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface BomsProductConfigModelYearDao extends IService<BomsProductConfigModelYearEntity> {

    /**
     * 获取ModelYearConfig
     *
     * @param model     车型
     * @param modelYear Model Year
     * @return BomsModelYearConfigEntity
     */
    BomsProductConfigModelYearEntity getModelYearConfig(String model, String modelYear);

    /**
     * 查询所有ModelYearConfig
     *
     * @return BomsModelYearConfigEntity列表
     */
    List<BomsProductConfigModelYearEntity> queryAll();

    /**
     * 根据车型查询
     *
     * @param model 车型
     * @return BomsProductConfigModelYearEntity列表
     */
    List<BomsProductConfigModelYearEntity> queryByModel(String model);

}
