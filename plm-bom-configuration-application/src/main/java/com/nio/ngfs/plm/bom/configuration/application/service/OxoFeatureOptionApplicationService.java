package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
public interface OxoFeatureOptionApplicationService {

    /**
     * 查询同一排序分组的Feature/Option
     *
     * @param model             车型
     * @param featureOptionCode Feature/Option Code
     * @return OxoFeatureOptionAggr列表
     */
    List<OxoFeatureOptionAggr> querySameSortGroupFeatureOption(String model, String featureOptionCode);

    /**
     * 在最新Release版本OXO中查询存在的Feature/Option
     *
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     * @return Feature/Option Code集合
     */
    Set<String> queryExistFeatureOptionInLastedReleaseSnapshot(List<OxoFeatureOptionAggr> featureOptionAggrList);

    /**
     * 构建Feature/Option行的子节点列表
     *
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     */
    void buildFeatureOptionWithChildren(List<OxoFeatureOptionAggr> featureOptionAggrList);

    /**
     * 校验并删除打点
     *
     * @param featureOptionAggrList OxoFeatureOptionAggr列表
     * @return OxoOptionPackageAggr列表
     */
    Pair<List<OxoOptionPackageAggr>, List<String>> checkAndDeleteOptionPackage(List<OxoFeatureOptionAggr> featureOptionAggrList);


    /**
     * 根据 车型查询oxo working版本信息
     * @param modelCode
     * @para version
     * @return
     */
    OxoListQry queryOxoInfoByModelCode(String modelCode, String version,Boolean isMaturity);


    /**
     * 查询 未选中  featureCode
     * @param modelCode
     * @return
     */
    List<FeatureAggr> queryFeaturesByModel(String modelCode);


    /**
     *  根据车型 查询规则
     * @param modelCode
     * @return
     */
    List<String> checkRules(String modelCode);
}
