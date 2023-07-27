package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoFeatureOptionDao extends IService<BomsOxoFeatureOptionEntity> {

    /**
     * 根据车型和Feature Code列表查询
     *
     * @param model           车型
     * @param featureCodeList Feature Code列表
     * @return BomsOxoFeatureOptionEntity列表
     */
    List<BomsOxoFeatureOptionEntity> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList);

}
