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




    List<OxoFeatureOptionAggr> queryFeatureListsByModel(String modelCode);


}
