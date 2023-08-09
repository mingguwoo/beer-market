package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface BomsModelYearConfigDao extends IService<BomsModelYearConfigEntity> {

    /**
     * 获取ModelYearConfig
     *
     * @param model     车型
     * @param modelYear Model Year
     * @return BomsModelYearConfigEntity
     */
    BomsModelYearConfigEntity getModelYearConfig(String model, String modelYear);

}
