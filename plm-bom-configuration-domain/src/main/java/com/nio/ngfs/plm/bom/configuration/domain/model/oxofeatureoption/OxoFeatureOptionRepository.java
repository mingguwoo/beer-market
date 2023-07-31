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



    List<OxoFeatureOptionAggr> queryFeaturesByModel(String modelCode);


    /**
     * 根据modelCode查询未查询的 featureCode
     * @param modelCode
     * @return
     */
    List<OxoFeatureOptionAggr> queryFeatureListsByModel(String modelCode);


    /**
     * 根据 车型 过滤 软删除的行
     * @param modelCode
     * @return
     */
    List<OxoFeatureOptionAggr>  queryFeatureListsByModelAndSortDelete(String modelCode);


}
