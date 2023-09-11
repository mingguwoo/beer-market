package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoFeatureOptionRepository extends Repository<OxoFeatureOptionAggr, Long> {

    /**
     * 根据车型和Feature Code列表查询
     *
     * @param model           车型
     * @param featureCodeList Feature Code列表
     * @return OxoFeatureOptionAggr列表
     */
    List<OxoFeatureOptionAggr> queryByModelAndFeatureCodeList(String model, List<String> featureCodeList);

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<OxoFeatureOptionAggr> aggrList);

    /**
     * 批量删除
     *
     * @param aggrList 聚合根列表
     */
    void batchRemove(List<OxoFeatureOptionAggr> aggrList);



    /**
     * 根据modelCode查询未查询的 featureCode
     * @param modelCode
     * @return
     */
    List<OxoFeatureOptionAggr> queryFeatureListsByModel(String modelCode,List<String> roleNames);


    /**
     * 根据 车型 过滤 软删除的行
     * @param modelCode
     * @return
     */
    List<OxoFeatureOptionAggr>  queryFeatureListsByModelAndSortDelete(String modelCode,Boolean isSoftDelete);


    /**
     *  添加删除
     * @param oxoFeatureOptionAggrs
     */
    void insertOrUpdateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs);


    /**
     * 更新 数据
     * @param oxoFeatureOptionAggrs
     */
    void updateOxoFeatureOptions(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs);



    /**
     * 恢复软删除
     * @param ids
     * @param isDelete
     */
    void restoreOxoFeatureOptionByIds(List<Long> ids, Integer isDelete);


}
