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
     *
     * @param modelCode
     * @param featureCode
     * @param optionCode
     * @return
     */
    List<BomsProductConfigModelOptionEntity>  queryProductConfigModelOptionByModelOrFeatureOrOptionCode(String modelCode, String featureCode,
                                                                                                        String optionCode,List<String> optionCodes);
}
