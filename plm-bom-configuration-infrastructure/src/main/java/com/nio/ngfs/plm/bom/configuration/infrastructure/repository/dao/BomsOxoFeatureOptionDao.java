package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoFeatureOptionDao extends IService<BomsOxoFeatureOptionEntity> {

    /**
     * 根据Model Code 和 Feature Code去批量查询
     *
     * @param featureList
     * @param modelCode
     * @return BomsOxoFeatureOptionEntity列表
     */
    List<BomsOxoFeatureOptionEntity> getBaseVehicleOptions(List<String> featureList, String modelCode);


    /**
     * 根据车型和Feature Code列表查询
     *
     * @param model           车型
     * @param featureCodeList Feature Code列表
     * @return BomsOxoFeatureOptionEntity列表
     */
    List<BomsOxoFeatureOptionEntity> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList);


    /**
     * 根据车型 查询oxo 行
     * true
     * @param modelCode
     * @return
     */
    List<BomsOxoFeatureOptionEntity> queryOxoFeatureOptionByModel(String modelCode, Boolean sortDelete);


    /**
     * 批量更新
     * @param oxoRowInfoAggrs
     */
    void updateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoRowInfoAggrs);


    /**
     * 恢复软删除
     * @param ids
     * @param isDelete
     */
    void restoreOxoFeatureOptionByIds(List<Long> ids, Integer isDelete);


    /**
     * 批量新增更新 防止重复
     * @param entities
     */
    void insertOrUpdateBomsOxoFeature(List<BomsOxoFeatureOptionEntity> entities);

}
