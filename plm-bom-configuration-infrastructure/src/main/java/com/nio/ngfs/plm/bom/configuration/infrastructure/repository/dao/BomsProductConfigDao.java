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
     * 根据车型查询
     *
     * @param model 车型
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByModel(String model);

}
