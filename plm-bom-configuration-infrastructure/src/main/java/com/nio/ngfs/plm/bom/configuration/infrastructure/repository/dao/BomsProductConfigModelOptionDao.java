package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public interface BomsProductConfigModelOptionDao extends IService<BomsProductConfigModelOptionEntity> {

    /**
     * 根据车型和Option Code查询
     *
     * @param modelCode  车型
     * @param optionCode Option Code
     * @return BomsProductConfigModelOptionEntity
     */
    BomsProductConfigModelOptionEntity getByModelAndOptionCode(String modelCode, String optionCode);

    /**
     * 根据车型查询
     *
     * @param modelCode 车型
     * @return BomsProductConfigModelOptionEntity列表
     */
    List<BomsProductConfigModelOptionEntity> queryByModel(String modelCode);

    /**
     * @param modelCode
     * @param featureCode
     * @param optionCode
     * @return
     */
    List<BomsProductConfigModelOptionEntity> queryProductConfigModelOptionByModelOrFeatureOrOptionCode(String modelCode, String featureCode,
                                                                                                       String optionCode, List<String> optionCodes);
}
